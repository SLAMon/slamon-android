FROM java:8-jdk

RUN groupadd -r slamon && useradd -r -d /workspace -m -g slamon slamon

RUN apt-get update && \
    apt-get upgrade -y && \
    apt-get install -y wget lib32z1 lib32stdc++-4.8-dev && \
    apt-get -y clean

WORKDIR /opt
RUN wget -q http://dl.google.com/android/android-sdk_r24.3.4-linux.tgz && \
    tar xzf android-sdk_r24.3.4-linux.tgz && \
    rm -f android-sdk_r24.3.4-linux.tgz
ENV ANDROID_HOME /opt/android-sdk-linux
ENV PATH ${PATH}:${ANDROID_HOME}/tools:${ANDROID_HOME}/platform-tools
RUN echo y | android update sdk --all --filter platform-tools,build-tools-22.0.0,android-22,extra-android-support,extra-google-m2repository,extra-android-m2repository --no-ui --force

ADD . /workspace
RUN chown -R slamon:slamon /workspace
USER slamon
WORKDIR /workspace
RUN ./gradlew --version
RUN ./gradlew assemble
