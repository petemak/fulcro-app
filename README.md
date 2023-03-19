# Basic Fulcro Application
### Created while following along on the *Fulcro Developer Guide found at: * https://book.fulcrologic.com/


**Shows use of** the following stack to build sull-stack web application in a dynamic short feedback loop:
  * Clojure - https://clojure.org/
  * ClojureScript - https://clojurescript.org/
  * Fulcro - http://fulcro.fulcrologic.com
  * Shadow-cljs - https://github.com/thheller/shadow-cljs
  * Pathom/EQL - https://pathom3.wsscode.com

The stack is intended to enable the "quick development story",
that is getting hot code reload to update the UI whenever source code changes. 

###
**Running the UI/CLJS project**
+ Go to root directory fulcro-app
+ Install dependencies ..shadow-cljs, REACT, etc

  ```
  $ npm install shadow-cljs react react-dom --save
  ```

+ Launch shadow-cljs, HTTP  and nREPL servers

  ```
  $ npx shadow-cljs server
  ```
  
+ Open shadow-cljs build UI on http://localhost:9630
+ Got to **Build** -> **main** -> and **watch**
+ view UI on HTTP server on http://localhost:8000
+ Eventually connect IDE to REPL sever on port 9000
 > Alt-x cider-connect-cljs ...

**Running the backend/CLJ project**
+ Opne a REPL or cider-jack-in 
* The HTTP server is implemented in _server.clj_ and can be started with by calling the _start_ function during development
  The start function is conveniently placed in the dev/user.clj and loaded at start of the REPL
     user> (start)
     2023-03-19T17:57:32.616Z Ideapad5iPro INFO [app.server:49] - Starting server on port 3000...
+ The applicaiton is then available http://localhost:3000/index.html
