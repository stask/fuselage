# {{name}}

## development environment

    If you use Emacs and cider (you really should), and you configured custom CLJS repl `(setq cider-cljs-lein-repl "(do (use 'figwheel-sidecar.repl-api) (start-figwheel!) (cljs-repl))")`, you can use following:

    * open project.clj in Emacs
    * hit Ctrl-c Shift-Alt-j to start both Clojure REPL and ClojureScript REPL.
      It may take some time to start, especially if you do it for the first time (downloading dependencies)
    * in the Clojure REPL (the one without CLJS in the name), run (reset)
    * open http://localhost:8080 in the browser. You will notice that browser connects to the ClojureScript REPL
    * start making changes.
      ClojureScript and CSS changes will appear immediately in the browser when you save them. Clojure changes will require re-running (reset) in the Clojure REPL buffer. You can also just re-evaluate pieces of code (Ctrl-c Ctrl-c) without running (reset).
