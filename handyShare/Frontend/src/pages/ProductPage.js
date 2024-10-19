import React from 'react';

export default function ProductPage() {
    const reviews = [
        { user: 'Alice', comment: 'Great product!' },
        { user: 'Bob', comment: 'Very useful and affordable.' },
    ];

    return (
        <div className="min-h-screen flex flex-col">
            <header className="bg-gray-800 text-white p-4 flex justify-between items-center">
                <div className="flex items-center">
                    <div className="text-xl font-bold">handyShare</div>
                </div>
                <nav className="space-x-4 flex items-center">
                    <a href="/lendings" className="hover:underline">Lendings</a>
                    <a href="/borrowings" className="hover:underline">Borrowings</a>
                    <a href="/profile" className="hover:underline">
                        Profile
                    </a>
                </nav>
            </header>
            <main className="flex-grow p-8 grid grid-cols-3 gap-8">
                <div className="bg-gray-100 p-4">
                    <img src="/path/to/product-image.jpg" alt="Product" className="w-full h-64 object-cover" />
                    <div className="mt-4">
                        <h3 className="text-lg font-bold">Transaction</h3>
                        <p>Transaction Time: 2 hours</p>
                        <button className="mt-2 bg-blue-500 text-white p-2 rounded">Rent Now</button>
                    </div>
                </div>
                <div className="flex flex-col justify-start">
                    <div className="flex items-center">
                        <div>
                            <h2 className="text-2xl font-bold">Product Name</h2>
                            <p className="text-lg">Price: $20/hour</p>
                        </div>
                    </div>
                    <div className="mt-4">
                        <label>From</label>
                        <input type="date" className="border p-2 mt-1" />
                        <div className="mt-4">
                            <h3 className="text-lg font-bold">Description</h3>
                            <p>Experience the ultimate sound quality with our state-of-the-art headphones. Designed for comfort and precision, these headphones deliver crystal clear audio and deep bass for an immersive listening experience.</p>
                        </div>
                    </div>
                </div>
                <div>
                    <div className="mb-4">
                        <h3 className="text-lg font-bold">Lender Information</h3>
                        <p>Name: John Doe</p>
                        <img src="/path/to/lender-image.jpg" alt="Lender" className="w-16 h-16 rounded-full mt-2" />
                        <p>Rating: 4.5</p>
                        <p>Location: New York, NY</p>
                    </div>
                    <div className="mt-4">
                        <h3 className="text-lg font-bold">Lender's Location</h3>
                        <p>Google map location goes here.</p>
                    </div>
                    <div className="mt-4">
                        <h3 className="text-lg font-bold">Reviews</h3>
                        {reviews.map((review, index) => (
                            <div key={index} className="mt-2">
                                <p><strong>{review.user}:</strong> {review.comment}</p>
                            </div>
                        ))}
                    </div>
                </div>
            </main>
        </div>
    );
}
