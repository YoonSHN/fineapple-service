FROM node:20 as build
WORKDIR /app
COPY . .
RUN npm install && npm run build

FROM nginx:stable-alpine
COPY --from=build /app/dist/fineapple-chat.js /usr/share/nginx/html/js/
