#Use official Node.js image as base
FROM node:20 AS builder

#Set working directory inside the container
WORKDIR /app

#Copy package files and install dependencies
COPY package*.json ./
RUN npm install

#Copy the rest of the app source
COPY . .

#Build the angular app
RUN npm run build -- --output-path=dist/app --configuration=production

#Serve the built app with a lightweight web server
FROM nginx:alpine
COPY --from=builder /app/dist/app/browser /usr/share/nginx/html

#Expose port 80 for Nginx
EXPOSE 80

#Start Nginx
CMD ["nginx","-g","daemon off;"]
