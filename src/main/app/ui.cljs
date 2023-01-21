

(ns app.ui
  (:require [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
            [com.fulcrologic.fulcro.dom :as dom]))


(def ui-data {:friends {:list/label "Friends"
                        :list/people [{:person/id 1 :person/name "sally" :person/age 32}
                                      {:person/id 2 :person/name "Joe"   :person/age 41}]}
              :enemies {:list/label "Enemies"
                        :list/people [{:person/id 3 :person/name "Fred" :person/age 28}
                                      {:person/id 4 :person/name "Boby" :person/age 55}]}})
;;-----------------------------------------------------------
;; Person component
;; Data is passed by destructuring the second argument of defsc
;;-----------------------------------------------------------
(defsc Person [this {:person/keys [id name age]}]
  {:initial-state (fn [{:keys [id name age]}] {:person/id id :person/name name :person/age age})}
  (dom/div
   (dom/p "Id: " id)
   (dom/p "Name: " name)
   (dom/p "Age: " age)))


(def ui-person (comp/factory Person {:keyfn :person/id}))


;;-----------------------------------------------------------
;; Person list component
;; prints out a list of people
;;-----------------------------------------------------------
(defsc PersonList [this {:list/keys [label people]}]
  {:initial-state (fn [{:keys [label]}] {:list/label label
                                         :list/people (if (= label "Friends")
                                                        [(comp/get-initial-state Person {:id 1 :name "Sally" :age 32})
                                                         (comp/get-initial-state Person {:id 2 :name "Joe"   :age 41})]
                                                        [(comp/get-initial-state Person {:id 3 :name "Fred"  :age 28})
                                                         (comp/get-initial-state Person {:id 4 :name "Boby"  :age 55} )])} )}
  (dom/div
   (dom/h3 "List: " label)
   (dom/h4 "Element count: " (count people))
   (dom/ul
    (map ui-person people))))

(def ui-person-list (comp/factory PersonList))


;;-----------------------------------------------------------
;; Root component
;;-----------------------------------------------------------
(defsc Root [this {:keys [friends enemies]}]
  {:initial-state (fn [params] {:friends (comp/get-initial-state PersonList {:label "Friends"})
                                :enemies (comp/get-initial-state PersonList {:label "Enemies"})})}
  (dom/div
   (dom/h2 "Fulcro Basic Fullstack Application")
   (dom/hr)
   (dom/div
    (dom/h3 "Tech Stack:")
    (dom/ul
     (dom/li "Clojure - https://clojure.org/")
     (dom/li "ClojureScript - https://clojurescript.org/")
     (dom/li "Fulcro - http://fulcro.fulcrologic.com")
     (dom/li "Shadow-cljs - https://github.com/thheller/shadow-cljs")
     (dom/li "Pathom/EQL - https://pathom3.wsscode.com")))
   (dom/hr)
   (dom/div
    (ui-person-list friends )
    (ui-person-list enemies))))

