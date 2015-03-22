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

```bash
boot cljs
```

This ensures that the target directory `target/` directory (from which the
project is served) exists.

## Use

```bash
boot dev
```

This will serve the project at http://localhost:3000, watch the sources, build and reload your project.

Now open http://localhost:3000 and try changing something in core.cljs!
To use cljs-repl in another terminal do:

```
boot repl --client
boot.user=> (start-repl)
; Refresh the page
boot.user=> (js/console.log "Hello!")
boot.user=> (in-ns 'boot-setup.core)
boot-setup.core=> (swap! app-state assoc :cong "Profit!")
boot-setup.core=> (another-root)
boot-setup.core=> (main)
```

[installboot]:       https://github.com/boot-clj/boot#install
[blog-post]:         http://adzerk.com/blog/2014/11/clojurescript-builds-rebooted/
[boot]:              https://github.com/boot-clj/boot
[boot-cljs-example]: https://github.com/adzerk/boot-cljs-example
