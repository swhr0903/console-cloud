FROM java:8-jre-alpine

COPY ./target/*.jar console.jar
CMD java -jar -Ddruid.config.decrypt=true tp-check.jar

#设置时区
RUN  echo 'http://mirrors.ustc.edu.cn/alpine/v3.5/main' > /etc/apk/repositories \
    && echo 'http://mirrors.ustc.edu.cn/alpine/v3.5/community' >>/etc/apk/repositories \
&& apk update && apk add tzdata \
&& ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
&& echo "Asia/Shanghai" > /etc/timezone

EXPOSE 8080
