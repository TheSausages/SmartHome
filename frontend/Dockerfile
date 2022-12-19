# pull official base image
FROM node:14.16.1-alpine as build

# add `/app/node_modules/.bin` to $PATH
ENV PATH node_modules/.bin:$PATH

# Copy neccessery files and install dependencies
COPY package.json ./
COPY package-lock.json ./
COPY . ./
RUN npm i

# Build the app
RUN npm run build

# Use nginx
FROM nginx:1.22.1-alpine

# Copy the build, env file and nginx cofnig file
COPY --from=build build /usr/share/nginx/html
COPY --from=build .env /usr/share/nginx/html/.env
COPY nginx.conf /etc/nginx/conf.d/default.conf

# install nodejs & npm
RUN apk add --update nodejs
RUN apk add --update npm

# install runtime-env-cra package
RUN npm i -g runtime-env-cra

WORKDIR /usr/share/nginx/html

# Set the run command
CMD ["/bin/sh", "-c", "runtime-env-cra && nginx -g \"daemon off;\""]