FROM --platform=$BUILDPLATFORM node:24-alpine3.21 AS builder
WORKDIR /build/fe
COPY package.json /build/fe/package.json
COPY package-lock.json /build/fe/package-lock.json
RUN npm ci --force
COPY . .
ENV HOST=0.0.0.0
ENV PORT=80
RUN npm run build

FROM nginx:stable-alpine AS prod
COPY --from=builder /build/fe/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
