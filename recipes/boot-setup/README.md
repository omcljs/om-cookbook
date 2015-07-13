# Problem

You want to start a new om project with boot-cljs but don't know how.

# Solution

Read [introductory blog post][blog-post] on boot-cljs.

## Prepare

[Install boot][installboot]. Then, in a terminal:

```bash
boot -u
```

This will update boot to the latest stable release version.

## Use

To serve the project at http://localhost:3000, watch the sources,
build and reload your project, run:

```bash
boot dev
```

The `dev` task as well as project dependencies are defined in
[build.boot](./build.boot)-file. The `dev` task uses multiple other boot tasks:

- [boot-cljs] to compile Cljs sources
- [boot-cljs-repl] to provide nrepl server with Cljs middlewares
- [boot-http] to serve http and js files
- [boot-reload] to reload changed files

You should read the `build.boot` to see how the tasks are combined. The task
options should be documented on the file.

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

## More resources

- More example projects
   - [boot-cljs-example]
   - [Saapas][saapas]
- Best places to ask for help are `#hoplon` on Freenode IRC and `#boot` on
[Clojurians Slack][clojurians]

[installboot]:       https://github.com/boot-clj/boot#install
[blog-post]:         http://adzerk.com/blog/2014/11/clojurescript-builds-rebooted/
[boot]:              https://github.com/boot-clj/boot
[boot-cljs-example]: https://github.com/adzerk/boot-cljs-example
[boot-cljs]:         https://github.com/adzerk-oss/boot-cljs
[boot-cljs-repl]:    https://github.com/adzerk-oss/boot-cljs-repl
[boot-reload]:       https://github.com/adzerk-oss/boot-reload
[boot-http]:         https://github.com/pandeiro/boot-http
[saapas]:            https://github.com/Deraen/saapas
[clojurians]:        https://clojurians.slack.com/
