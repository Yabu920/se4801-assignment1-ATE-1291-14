#!/bin/sh

set -eu

BASE_DIR=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
WRAPPER_DIR="$BASE_DIR/.mvn/wrapper"
MAVEN_VERSION="3.9.9"
MAVEN_BASE="apache-maven-$MAVEN_VERSION"
MAVEN_HOME="$WRAPPER_DIR/$MAVEN_BASE"
MAVEN_ARCHIVE="$WRAPPER_DIR/$MAVEN_BASE-bin.tar.gz"
DOWNLOAD_URL="https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/$MAVEN_VERSION/$MAVEN_BASE-bin.tar.gz"

if [ ! -x "$MAVEN_HOME/bin/mvn" ]; then
  mkdir -p "$WRAPPER_DIR"
  if command -v curl >/dev/null 2>&1; then
    curl -fsSL "$DOWNLOAD_URL" -o "$MAVEN_ARCHIVE"
  elif command -v wget >/dev/null 2>&1; then
    wget -q "$DOWNLOAD_URL" -O "$MAVEN_ARCHIVE"
  else
    echo "curl or wget is required to download Maven." >&2
    exit 1
  fi
  tar -xzf "$MAVEN_ARCHIVE" -C "$WRAPPER_DIR"
fi

exec "$MAVEN_HOME/bin/mvn" "$@"
