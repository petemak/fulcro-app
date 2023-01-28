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
**Running the project**
+ Go to root directory fulcro-app
+ Launch shadow-cljs, HTTP  and nREPL servers 
  ~px shadow-cljs server~
+ Open shadow-cljs build UI on http://localhost:9630
+ Got to build -> main -> and watch
+ view UI on HTTP server on http://localhost:8000
+ Eventually connect IDE to L sever on port 9000
 ~alt-x cider-connect-clj ...~
