(ns optimus-autoprefixer.core-test
  (:require [midje.sweet :refer :all]
            [optimus-autoprefixer.core :refer :all]
            [test-with-files.core :refer [with-files public-dir *last-modified*]]))

(fact
 "It prefixes CSS."

 (prefix-css "body { transform: rotate(45deg); }")
 => "body { -webkit-transform: rotate(45deg); transform: rotate(45deg); }"

 (prefix-css "body { transition: transform 200ms ease; }")
 => "body { -webkit-transition: -webkit-transform 200ms ease; transition: -webkit-transform 200ms ease; transition: transform 200ms ease; transition: transform 200ms ease, -webkit-transform 200ms ease; }")

(fact
 "It prefixes optimus CSS assets"
 (prefix-css-assets [{:path "style.css" :contents "body { transform: rotate(90deg); }"}
                     {:path "stail.css" :contents "body { transform: rotate(180deg); }"}])
 => [{:path "style.css" :contents "body { -webkit-transform: rotate(90deg); transform: rotate(90deg); }"}
     {:path "stail.css" :contents "body { -webkit-transform: rotate(180deg); transform: rotate(180deg); }"}])

(fact
 "It throws exceptions"
 (prefix-css "body {")
 => (throws Exception "<css input>:1:1: Unclosed block (line 1, col -1)"))

(fact
 "It includes path in error messages."
 (prefix-css-assets [{:path "style.css" :contents "body {"}])
 => (throws Exception "Exception in style.css: <css input>:1:1: Unclosed block (line 1, col -1)"))

(fact
 "You can specify browsers"

 (prefix-css "body { transform: rotate(45deg); }" {:browsers ["last 2 versions"]})
 => "body { transform: rotate(45deg); }"

 (prefix-css-assets [{:path "style.css" :contents "body { transform: rotate(90deg); }"}]
                    {:browsers ["last 2 versions"]})
 => [{:path "style.css" :contents "body { transform: rotate(90deg); }"}])
