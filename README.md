# optimus-autoprefixer [![Build Status](https://secure.travis-ci.org/magnars/optimus-autoprefixer.png)](http://travis-ci.org/magnars/optimus-autoprefixer)

A [autoprefixer](https://github.com/postcss/autoprefixer) CSS prefix middleware for [Optimus](http://github.com/magnars/optimus).

## Install

Add `[optimus-autoprefixer "0.1.0"]` to `:dependencies` in your `project.clj`.

## Usage

Require `[optimus-autoprefixer.core :as prefixer]`, then add it to your Optimus
middleware stack:

```clj
(defn optimize-all [assets options]
  (-> assets
      (prefixer/prefix-css-assets {:browsers ["> 1%" "IE 8"]})
      (optimizations/all options)))
```

Then use this in place of `optimizations/all` in your `optimus/wrap` statement:

```clj
(-> app
    (optimus/wrap get-assets
                  (if (= :prod env) optimize optimizations/none)
                  (if (= :prod env) strategies/serve-frozen-assets strategies/serve-live-assets)))
```

## Contribute

Yes, please do.

One low hanging fruit is adding support for more options than just `browsers`,
see https://github.com/postcss/autoprefixer#options

Remember to add tests for your feature or fix, or I'll certainly break
it later.

#### Running the tests

`lein midje` will run all tests.

`lein midje :autotest` will run all the tests indefinitely. It sets up a
watcher on the code files. If they change, only the relevant tests will be
run again.

## License

Copyright © 2016 Magnar Sveen

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
