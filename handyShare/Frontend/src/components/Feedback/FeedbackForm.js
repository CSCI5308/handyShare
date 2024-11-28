import React, { useState, useEffect } from "react";
import axios from "axios";

const FeedbackForm = ({ productId, userId, onSubmit }) => {
  const [feedbackText, setFeedbackText] = useState("");
  const [rating, setRating] = useState(0);  // Initially set to 0 (no rating selected)
  const [image, setImage] = useState(null);
  const [errorMessage, setErrorMessage] = useState("");
  const [hoverRating, setHoverRating] = useState(0);  // Track hover state

  useEffect(() => {
    // Optional: fetch userId dynamically if it's not passed as prop
    if (!userId) {
      // You can fetch the userId from a global state or authentication context
      // Example:
      // setUserId(fetchUserIdFromContext());
    }
  }, [userId]);

  const handleStarClick = (starRating) => {
    setRating(starRating);  // Update the rating when a star is clicked
  };

  const handleStarHover = (starRating) => {
    setHoverRating(starRating);  // Update the hover rating
  };

  const handleStarLeave = () => {
    setHoverRating(0);  // Reset hover rating when the mouse leaves
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    // Validate feedback input
    if (!feedbackText || !rating) {
      setErrorMessage("Please provide feedback and a rating.");
      return;
    }

    // Prepare the form data to be sent to the backend
    const formData = new FormData();
    formData.append("userId", userId);  // User ID passed as prop or dynamically fetched
    formData.append("productId", productId);  // Product ID passed as prop
    formData.append("reviewText", feedbackText);
    formData.append("rating", rating);
    if (image) {
      formData.append("image", image);
    }

    try {
      // Make POST request to submit the review
      const response = await axios.post("/api/v1/all/feedback/create", formData, {
        headers: {
          "Content-Type": "multipart/form-data",  // Content type for file upload
        },
      });
      
      // Handle successful submission
      if (response.status === 200) {
        onSubmit(); // Callback function to refresh feedback data after submission
        setFeedbackText("");
        setRating(0);  // Reset rating after submission
        setImage(null);
        setErrorMessage(""); // Clear any error message
      } else {
        setErrorMessage("Failed to submit feedback. Please try again later.");
      }
    } catch (error) {
      console.error("Error submitting feedback:", error);
      setErrorMessage("Failed to submit feedback. Please try again later.");
    }
  };

  return (
    <div className="max-w-lg mx-auto p-4 border rounded-md shadow-lg">
      <h2 className="text-xl font-semibold text-center mb-4">Submit Feedback</h2>
      {errorMessage && (
        <div className="text-red-500 text-center mb-4">{errorMessage}</div>
      )}
      <form onSubmit={handleSubmit}>
        <div className="mb-4">
          <label className="block text-sm font-medium mb-2" htmlFor="feedbackText">
            Your Feedback:
          </label>
          <textarea
            id="feedbackText"
            className="w-full p-2 border rounded-md"
            rows="4"
            value={feedbackText}
            onChange={(e) => setFeedbackText(e.target.value)}
            required
          />
        </div>

        <div className="mb-4">
          <label className="block text-sm font-medium mb-2">Rating:</label>
          <div className="flex space-x-2">
            {[1, 2, 3, 4, 5].map((star) => (
              <span
                key={star}
                className={`cursor-pointer text-2xl 
                  ${star <= (hoverRating || rating) ? 'text-yellow-500' : 'text-gray-400'} 
                  ${star <= hoverRating ? 'text-yellow-300' : ''} 
                `}
                onClick={() => handleStarClick(star)}
                onMouseEnter={() => handleStarHover(star)}
                onMouseLeave={handleStarLeave}
              >
                ★
              </span>
            ))}
          </div>
        </div>

        <div className="mb-4">
          <label className="block text-sm font-medium mb-2" htmlFor="image">
            Upload an Image (optional):
          </label>
          <input
            type="file"
            id="image"
            className="w-full p-2 border rounded-md"
            onChange={(e) => setImage(e.target.files[0])}
          />
        </div>

        <button
          type="submit"
          className="w-full bg-blue-500 text-white p-2 rounded-md hover:bg-blue-600"
        >
          Submit Review
        </button>
      </form>
    </div>
  );
};

export default FeedbackForm;
