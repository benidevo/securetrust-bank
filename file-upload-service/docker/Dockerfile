ARG NODE_VERSION=18-alpine

FROM node:${NODE_VERSION}

ENV NODE_ENV=development

ARG APP_HOME=/app

WORKDIR ${APP_HOME}

RUN addgroup --system stb \
  && adduser --system --ingroup stb stb

RUN apk --no-cache add netcat-openbsd

COPY package*.json ./

RUN npm install

COPY --chown=stb:stb . ${APP_HOME}

RUN chown stb:stb ${APP_HOME}

USER stb

CMD ["sh", "./docker/entrypoint.sh"]
