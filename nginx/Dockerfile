FROM nginx:latest


COPY ./static/html /usr/share/nginx/html
COPY ./static/js /usr/share/nginx/html/js
COPY ./static/css /usr/share/nginx/html/css
COPY ./static/uploads /usr/share/nginx/html/uploads


COPY ./nginx.conf /etc/nginx/nginx.conf


RUN chown -R nginx:nginx /usr/share/nginx/html \
 && chmod -R 755 /usr/share/nginx/html \
 && find /usr/share/nginx/html -type f -exec chmod 644 {} \;
