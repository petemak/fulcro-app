(ns app.ui
  (:require [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
            [com.fulcrologic.fulcro.dom :as dom]
            [app.mutations :as api]))

;;
(def ui-data {:friends {:list/label "Friends"
                        :list/people [{:person/id 1 :person/name "sally" :person/age 32}
                                      {:person/id 2 :person/name "Joe"   :person/age 41}]}
              :enemies {:list/label "Enemies"
                        :list/people [{:person/id 3 :person/name "Fred" :person/age 28}
                                      {:person/id 4 :person/name "Boby" :person/age 55}]}})
;;-----------------------------------------------------------
;; Person component
;; Data is passed by destructuring the second argument of defsc
;; Initial state co-locates the initial desired part of the tree
;; with the component that uses it, here id, name and age
;;
;; :initial-state: starting initial state.
;;    a lambda that gets parameters (optionally from the parent)
;;    and returns a map representing the state of the component.
;; :query: For each scalar property in initial state,
;; there should be an identical simple property in your query.
;;
;;-----------------------------------------------------------
(defsc Person [this {:person/keys [id name age] :as props} {:keys [onDelete]}]
  {:query [:person/id :person/name :person/age]
   :ident (fn [] [:person/id (:person/id props)])}
  
  (dom/nav :.level
           (dom/div :.level-left.has-text-left
                    (dom/div :.level-item
                             (dom/div
                              (dom/p :.title  name)
                              (dom/p :.subtitle.is-5 (str "ID: " id " - ") (str "Age: " age))))
                    (dom/div :.level-item
                              (dom/button :.button.is-danger.is-light {:onClick  #(onDelete id)} "x" ))
                    ))

  )


;; Element factory
(def ui-person (comp/factory Person {:keyfn :person/id}))

;;-----------------------------------------------------------
;; Person list component
;; prints out a list of people
;;
;; :initial-state
;; :query a scalar label and a join for people that composes in Person
;;
;;-----------------------------------------------------------
(defsc PersonList [this {:list/keys [id label people] :as props}]
  {:query [:list/id :list/label {:list/people (comp/get-query Person)} ]
   :ident (fn [] [:list/id (:list/id props)])}
  
  (let [delete-person (fn [person-id] (comp/transact! this [(api/delete-person {:list/id id :person/id person-id})]))]
   (dom/div :.content.is-normal
    (dom/h3 "List: " label)
    (dom/h4 "Element count: " (count people))
    (dom/ul
     (mapv #(ui-person (comp/computed % {:onDelete delete-person})) people)))))

(def ui-person-list (comp/factory PersonList))


;;-----------------------------------------------------------
;; Root component
;;-----------------------------------------------------------
(defsc Root [this {:keys [friends enemies]}]
  {:query [{:friends (comp/get-query PersonList)}
           {:enemies (comp/get-query PersonList)}]
   :initial-state {}}
  
  (dom/div
   (dom/div :.block
            (dom/h2 :.title.is-2 "Basic Fullstack Application based on Fulcro"))
   (dom/div :.block
            (dom/div :.content
                        (dom/h3 :.subtitle "Tech Stack")
                        (dom/p "The stack is intended to enable the \"quick development story \", that is,
                                getting hot code reload to update the UI whenever source code changes.")
                        (dom/ul
                         (dom/li  "Clojure - https://clojure.org/")
                         (dom/li  "ClojureScript - https://clojurescript.org/")
                         (dom/li  "Fulcro - http://fulcro.fulcrologic.com")
                         (dom/li  "Shadow-cljs - https://github.com/thheller/shadow-cljs")
                         (dom/li  "Pathom/EQL - https://pathom3.wsscode.com"))))
   
   (dom/div :.block
           
            (dom/div :.columns
                     (dom/div :.column.is-half
                              (dom/h4 "Friends: " friends)
                              (when friends
                                (ui-person-list friends )))
                     (dom/div :.column.is-half
                              (dom/h4  "Enemies: " enemies)
                              (when enemies  
                                (ui-person-list enemies)))))))

