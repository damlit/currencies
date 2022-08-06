FROM node:14.17.1 as build
COPY package.json .
COPY yarn.lock .
RUN yarn install
COPY public/ ./public/
COPY src/ ./src/
RUN yarn build
EXPOSE 3000
ENTRYPOINT ["yarn", "start"]