FROM java:8-jre-alpine

#设置时区
RUN  echo 'http://mirrors.ustc.edu.cn/alpine/v3.8/main' > /etc/apk/repositories \
    && echo 'http://mirrors.ustc.edu.cn/alpine/v3.8/community' >>/etc/apk/repositories \
    && apk update && apk add tzdata \
    && ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone

#定义nginx版本
ENV NGINX_VERSION 1.14.0

ENV MIRROR_URL http://mirrors.ustc.edu.cn/alpine/
ENV MIRROR_URL_BACKUP http://alpine.gliderlabs.com/alpine/
ENV MIRROR_URL_SLOWEST http://dl-cdn.alpinelinux.org/alpine/

#准备安装环境
RUN echo '' > /etc/apk/repositories
RUN echo "${MIRROR_URL}v3.3//main"     >> /etc/apk/repositories
RUN echo "${MIRROR_URL}v3.3//community" >> /etc/apk/repositories
RUN echo '185.31.17.249 github.com' >> /etc/hosts
RUN echo '202.141.160.110 mirrors.ustc.edu.cn' >> /etc/hosts
RUN echo '206.251.255.63 nginx.org' >> /etc/hosts

RUN apk add --no-cache --virtual .build-deps \
    gcc \
    libc-dev \
    make \
    openssl-dev \
    pcre-dev \
    zlib-dev \
    linux-headers \
    curl \
    jemalloc-dev \
    gd-dev \
    git

RUN mkdir -p /var/run/nginx/
RUN wget -c http://nginx.org/download/nginx-${NGINX_VERSION}.tar.gz
RUN git clone https://github.com/cuber/ngx_http_google_filter_module.git
RUN git clone https://github.com/yaoweibin/ngx_http_substitutions_filter_module.git
RUN git clone https://github.com/aperezdc/ngx-fancyindex.git
RUN git clone https://github.com/yaoweibin/nginx_upstream_check_module.git

#进行编译安装，同时打上补丁
RUN tar -xzvf nginx-${NGINX_VERSION}.tar.gz
RUN cd nginx-${NGINX_VERSION} && patch -p1 < /nginx_upstream_check_module/check_${NGINX_VERSION}+.patch

#去除nginx的对外版本号
RUN cd nginx-${NGINX_VERSION} && sed -i -e 's/${NGINX_VERSION}//g' -e 's/nginx\//ERROR/g' -e 's/"NGINX"/"ERROR"/g' src/core/nginx.h

#编译nginx
RUN cd nginx-${NGINX_VERSION} && ./configure --prefix=/usr/local/nginx \
--sbin-path=/usr/local/nginx/sbin/nginx \
--conf-path=/usr/local/nginx/conf/nginx.conf \
--pid-path=/usr/local/nginx/nginx.pid \
--with-pcre \
--with-ipv6 \
--with-http_ssl_module \
--with-http_flv_module \
--with-http_v2_module \
--with-http_realip_module \
--with-http_stub_status_module \
--with-http_gzip_static_module \
--with-http_stub_status_module \
--with-http_mp4_module \
--with-http_image_filter_module \
--with-http_addition_module \
--with-http_sub_module  \
--with-http_dav_module  \
--with-stream \
--http-client-body-temp-path=/usr/local/nginx/client/ \
--http-proxy-temp-path=/usr/local/nginx/proxy/ \
--http-fastcgi-temp-path=/usr/local/nginx/fcgi/ \
--http-uwsgi-temp-path=/usr/local/nginx/uwsgi \
--http-scgi-temp-path=/usr/local/nginx/scgi \
--add-module=../ngx_http_google_filter_module \
--add-module=../ngx_http_substitutions_filter_module \
--add-module=../ngx-fancyindex \
--add-module=../nginx_upstream_check_module \
--with-ld-opt="-ljemalloc"

RUN cd nginx-${NGINX_VERSION} && make -j $(awk '/processor/{i++}END{print i}' /proc/cpuinfo) && make install

#设置一些工作目录
RUN mkdir -p /usr/local/nginx/cache/
RUN mkdir -p /usr/local/nginx/temp/
RUN rm -rf ../{ngx*,nginx*}

COPY conf.d/nginx.conf /usr/local/nginx/conf/nginx.conf

#启动nginx，保留一个前台进程，以免被docker强制退出
RUN addgroup -S nginx && adduser -S -G nginx nginx
CMD ./usr/local/nginx/sbin/nginx -c conf/nginx.conf && tail -f /usr/local/nginx/logs/error.log