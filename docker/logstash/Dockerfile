FROM openjdk:8-jre-alpine

#设置时区
RUN  echo 'http://mirrors.ustc.edu.cn/alpine/v3.8/main' > /etc/apk/repositories \
    && echo 'http://mirrors.ustc.edu.cn/alpine/v3.8/community' >>/etc/apk/repositories \
    && apk update && apk add tzdata \
    && ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone

# ensure logstash user exists
RUN addgroup -S logstash && adduser -S -G logstash logstash

# install plugin dependencies
RUN apk add --no-cache \
# env: can't execute 'bash': No such file or directory
		bash \
		libc6-compat \
		libzmq

# grab su-exec for easy step-down from root
RUN apk add --no-cache 'su-exec>=0.2'

# https://www.elastic.co/guide/en/logstash/5.0/installing-logstash.html#_apt
# https://artifacts.elastic.co/GPG-KEY-elasticsearch
ENV GPG_KEY 46095ACC8548582C1A2699A9D27D666CD88E42B4

ENV LOGSTASH_PATH /usr/share/logstash/bin
ENV PATH $LOGSTASH_PATH:$PATH

ENV LOGSTASH_VERSION 6.4.3
ENV LOGSTASH_TARBALL="https://artifacts.elastic.co/downloads/logstash/logstash-${LOGSTASH_VERSION}.tar.gz" \
	LOGSTASH_TARBALL_ASC="https://artifacts.elastic.co/downloads/logstash/logstash-${LOGSTASH_VERSION}.tar.gz.asc" \
	LOGSTASH_TARBALL_SHA512="39bab1b9b0c9f0006c2ad7aa152b3863179030352202ec0c4c4a887bdeb98d4c34127699655cfc7c5b9f44119b45de9629bfacc670d520dcf266b3298fdf2484"

RUN set -ex; \
	\
	if [ -z "$LOGSTASH_TARBALL_SHA512" ] && [ -z "$LOGSTASH_TARBALL_ASC" ]; then \
		echo >&2 'error: have neither a SHA1 _or_ a signature file -- cannot verify download!'; \
		exit 1; \
	fi; \
	\
	apk add --no-cache --virtual .fetch-deps \
		ca-certificates \
		gnupg \
		openssl \
		tar \
	; \
	\
	wget -O logstash.tar.gz "$LOGSTASH_TARBALL"; \
	\
	if [ "$LOGSTASH_TARBALL_SHA512" ]; then \
		echo "$LOGSTASH_TARBALL_SHA512 *logstash.tar.gz" | sha512sum -c -; \
	fi; \
	\
	if [ "$LOGSTASH_TARBALL_ASC" ]; then \
		wget -O logstash.tar.gz.asc "$LOGSTASH_TARBALL_ASC"; \
		export GNUPGHOME="$(mktemp -d)"; \
		gpg --keyserver ha.pool.sks-keyservers.net --recv-keys "$GPG_KEY"; \
		gpg --batch --verify logstash.tar.gz.asc logstash.tar.gz; \
		rm -rf "$GNUPGHOME" logstash.tar.gz.asc; \
	fi; \
	\
	dir="$(dirname "$LOGSTASH_PATH")"; \
	\
	mkdir -p "$dir"; \
	tar -xf logstash.tar.gz --strip-components=1 -C "$dir"; \
	rm logstash.tar.gz; \
	\
	apk del .fetch-deps; \
	\
	export LS_SETTINGS_DIR="$dir/config"; \
# if the "log4j2.properties" file exists (logstash 5.x), let's empty it out so we get the default: "logging only errors to the console"
	if [ -f "$LS_SETTINGS_DIR/log4j2.properties" ]; then \
		cp "$LS_SETTINGS_DIR/log4j2.properties" "$LS_SETTINGS_DIR/log4j2.properties.dist"; \
		truncate -s 0 "$LS_SETTINGS_DIR/log4j2.properties"; \
	fi; \
	\
# set up some file permissions
	for userDir in \
		"$dir/config" \
		"$dir/data" \
	; do \
		if [ -d "$userDir" ]; then \
			chown -R logstash:logstash "$userDir"; \
		fi; \
	done; \
	\
	logstash --version /

EXPOSE 5044

COPY conf.d/logstash.conf /etc/logstash.conf

COPY docker-entrypoint.sh /usr/local/bin/
RUN chmod 777 /usr/local/bin/docker-entrypoint.sh && ln -s /usr/local/bin/docker-entrypoint.sh
ENTRYPOINT ["docker-entrypoint.sh"]

CMD ["-f", "/etc/logstash.conf"]