# Problem

You'd like to start a new Om project that includes
[Figwheel](https://github.com/bhauman/lein-figwheel) and
REPL-connected browser

# Solution

Use [chestnut](https://github.com/plexus/chestnut) template:

```clojure
$ lein new chestnut <name-of-your-project>
```

This creates project structure, adds needed dependencies to your
  `project.clj`. If you open up `README.md` that was created,
  you'll see step by step instructions of how to proceed further.

But the gist is:

1. Start the REPL
2. Do:

  ```clojure
  (run)
  ```
  This will start the webserver at the port 10555 and Figweheel server
  at port 3449, as specified in your `project.clj`:

  ```clojure
  :figwheel {:http-server-root "public"
             :port 3449
             :css-dirs ["resources/public/css"]}
  ```
  Your files will compile to `public` directory, and this is where
  you should put your CSS files too. You can modify it as needed.
  First time you exectute `run` it will take some time. Wait until
  files are compiled and you see `Successfully compiled
  "resources/public/app.js"`.
  You can open up the index page by going to `http://localhost:10555`
  in your browser.

3. Run  `(browser-repl)` to start the Weasel REPL server
  This will take you to ClojureScript REPL. Make sure you have the
  page opened in a browser. You can evaluate expressions here and
  see the resoluts in your browser.

4. Whenever you modify your CSS or cljs files, you'll see your page
being updated instantly.

5. Go to [Chestnut documention](http://plexus.github.io/chestnut/) for
   more information.
