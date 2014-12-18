# Problem

You want to start a new om project with boot-cljs but don't know how.

# Solution

Read [introductory blog post][blog-post] on boot-cljs.
Another example project on boot-cljs: [boot-cljs-example].

## Prepare

[Install boot][installboot].  Then, in a terminal:

```bash
boot -u
```

This will update boot to the latest stable release version (tested with 2.0.0-RC1). Since boot is
pre-alpha software at the moment, you should do this frequently.

## Use

```bash
boot dev
```

This will watch your project and build and reload your project.

To serve the project I just use python's simpleserver
(boot-cljs-example uses another solution):

```bash
cd target
python -m SimpleHTTPServer 6666
```

Now open http://localhost:6666 and try changing something core.cljs!
To use cljs-repl in another terminal do:

```
boot repl --client
boot.user=> (start-repl)
; Refresh the page
boot.user=> (js/console.log "Hello!")
```

[installboot]:       https://github.com/boot-clj/boot#install
[blog-post]:         http://adzerk.com/blog/2014/11/clojurescript-builds-rebooted/
[boot]:              https://github.com/boot-clj/boot
[boot-cljs-example]: https://github.com/adzerk/boot-cljs-example
