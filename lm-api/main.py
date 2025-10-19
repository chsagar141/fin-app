from fastapi import FastAPI, HTTPException
from pydantic import BaseModel, Field
import requests
import os
from typing import List, Optional
from datetime import datetime  # Added this import

# --- Configuration ---
# LM Studio API endpoint (usually http://localhost:1234/v1/chat/completions)
# You can set this as an environment variable or keep it here for local dev
LM_STUDIO_API_BASE_URL = os.getenv(
    "LM_STUDIO_API_BASE_URL", "http://localhost:1234/v1/chat/completions")
# Updated model name for clarity
LM_STUDIO_MODEL_NAME = os.getenv("LM_STUDIO_MODEL_NAME", "google/gemma-3-4b")

app = FastAPI(
    title="Financial AI Recommendation Service",
    description="Provides AI-driven financial recommendations using a local LM Studio API.",
    version="1.0.0"
)

# --- Request/Response Models ---


class FinancialItem(BaseModel):
    name: str
    price: float = Field(..., gt=0,
                         description="Price of the item, must be greater than 0")
    category: Optional[str] = None
    # Keeping as string for simplicity from SpringBoot, can be parsed to date if needed
    date_added: Optional[str] = None
    description: Optional[str] = None


class RecommendationRequest(BaseModel):
    user_id: int  # User ID from SpringBoot for context, though not directly used by AI for now
    items: List[FinancialItem] = Field(..., min_length=1,
                                       description="List of financial items for recommendation")


class RecommendationResponse(BaseModel):
    recommendation: str
    generated_at: str

# --- API Endpoint ---


@app.post("/generate_recommendation", response_model=RecommendationResponse)
async def generate_recommendation(request_data: RecommendationRequest):
    """
    Generates a financial recommendation based on user's financial items.
    """
    if not request_data.items:
        raise HTTPException(
            status_code=400, detail="No financial items provided for recommendation.")

    # 1. Construct a detailed prompt for the LM Studio API
    prompt_items_list = []
    total_spending = 0.0

    for item in request_data.items:
        item_str = f"- {item.name}: ${item.price:.2f}"
        if item.category:
            item_str += f" (Category: {item.category})"
        if item.description:
            item_str += f" - {item.description}"
        prompt_items_list.append(item_str)
        total_spending += item.price

    # Basic context for a monthly budget, assumes items are monthly for simplicity
    prompt_context = f"You are an AI financial advisor. The user has provided a list of recent expenses. " \
        f"Their total spending for these items is approximately ${total_spending:.2f}. " \
        f"Based on the following list, provide a detailed financial recommendation with actionable steps. " \
        f"Focus on identifying potential areas for saving, budgeting strategies, and general financial planning tips. " \
        f"Keep the tone encouraging and helpful. The recommendation should be at least 3 paragraphs long."

    user_message = "\n".join(prompt_items_list)

    full_prompt = f"{prompt_context}\n\nUser's Expenses:\n{user_message}\n\nRecommendation:"

    print(f"Sending prompt to LM Studio:\n{full_prompt}\n---")  # For debugging

    # 2. Make an HTTP request to the local LM Studio API
    headers = {"Content-Type": "application/json"}
    payload = {
        # This is often ignored by LM Studio's local server, but good practice
        "model": LM_STUDIO_MODEL_NAME,
        "messages": [
            {"role": "system", "content": "You are a helpful AI financial advisor."},
            {"role": "user", "content": full_prompt}
        ],
        "temperature": 0.7,
        "max_tokens": 500,  # Limit response length
        "stream": False  # Don't stream, get full response at once
    }

    try:
        response = requests.post(
            LM_STUDIO_API_BASE_URL, headers=headers, json=payload)
        response.raise_for_status()  # Raise HTTPError for bad responses (4xx or 5xx)
        lm_studio_response = response.json()

        # 3. Parse the response from LM Studio
        if "choices" in lm_studio_response and len(lm_studio_response["choices"]) > 0:
            recommendation_text = lm_studio_response["choices"][0]["message"]["content"].strip(
            )
            return RecommendationResponse(
                recommendation=recommendation_text,
                generated_at=datetime.now().isoformat()  # Fixed this line
            )
        else:
            raise HTTPException(
                status_code=500, detail="LM Studio did not return a valid recommendation.")

    except requests.exceptions.ConnectionError:
        raise HTTPException(
            status_code=503, detail="Could not connect to LM Studio API. Is it running?")
    except requests.exceptions.RequestException as e:
        raise HTTPException(
            status_code=500, detail=f"Error from LM Studio API: {e}")
    except Exception as e:
        raise HTTPException(
            status_code=500, detail=f"An unexpected error occurred: {e}")

# --- Health Check Endpoint (Optional but good practice) ---


@app.get("/health")
async def health_check():
    return {"status": "ok", "message": "Financial AI Service is running."}
