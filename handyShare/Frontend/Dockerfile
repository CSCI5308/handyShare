# Step 1: Use an official Node.js image as the base
FROM node:18

# Step 2: Set the working directory inside the container
WORKDIR /app

# Step 3: Copy package.json and package-lock.json (or yarn.lock)
COPY package*.json ./

# Step 4: Install dependencies
RUN npm install

# Step 5: Copy the rest of your application files
COPY . .

# Step 6: Expose the port your app runs on (default for most frontend apps is 3000)
EXPOSE 3000

# Step 7: Run the app (npm start will typically work for a React app)
CMD ["npm", "start"]