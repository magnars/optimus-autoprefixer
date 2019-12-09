#!/bin/sh

mkdir -p resources

if [ ! -f "resources/autoprefixer.js" ]; then
    curl -L --silent https://raw.githubusercontent.com/ai/autoprefixer-rails/9.7.3/vendor/autoprefixer.js -o resources/autoprefixer.js
fi
