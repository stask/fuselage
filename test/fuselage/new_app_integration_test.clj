(ns fuselage.new-app-integration-test
  (:require
   [clojure.java.io    :as io]
   [clojure.java.shell :as sh]
   [clojure.string     :as string]
   [clojure.test       :refer [is deftest]])
  (:import
   (java.util UUID)
   (java.io File)))

(def lein (or (System/getenv "LEIN_CMD") "lein"))

(def project-dir
  (-> (ClassLoader/getSystemResource *file*)
      io/file
      .getParent
      io/file
      .getParent))

;; This code was heavily inspired by Overtone's version, thanks!
;; https://github.com/overtone/overtone/blob/e3de1f7ac59af7fa3cf75d696fbcfc2a15830594/src/overtone/helpers/file.clj#L360
(defn mk-tmp-dir!
  "Creates a unique temporary directory on the filesystem. Typically in /tmp on
  *NIX systems. Returns a File object pointing to the new directory. Raises an
  exception of the directory couldn't be created after 100 tries."
  []
  (let [base-dir     (io/file (System/getProperty "java.io.tmpdir"))
        base-name    (str (UUID/randomUUID))
        tmp-base     (str base-dir File/separator base-name)
        max-attempts 100]
    (loop [num-attempts 1]
      (if (= num-attempts max-attempts)
        (throw (ex-info "Failed to create temporary directory"
                        {:attempt  num-attempts
                         :tmp-base tmp-base}))
        (let [tmp-dir-name (str tmp-base num-attempts)
              tmp-dir      (io/file tmp-dir-name)]
          (if (.mkdir tmp-dir)
            tmp-dir
            (recur (inc num-attempts))))))))

(def tempdir (mk-tmp-dir!))

(defn- sh-exits-successfully [full-app-name & args]
  (let [sh-result (sh/with-sh-dir full-app-name (apply sh/sh args))]
    (println (:out sh-result))
    (is (zero? (:exit sh-result))
        (format "Expected `%s` to exit successfully" (string/join \space args)))))

(deftest smoke
  (let [app-name "tech.stask/test-ns-app"
        full-app-name (.getPath (io/file tempdir "test-ns-app"))]
    (println (sh/with-sh-dir project-dir (sh/sh lein "install")))
    (println (sh/with-sh-dir tempdir
               (sh/sh lein
                      "new"
                      "fuselage"
                      app-name
                      "--snapshot"
                      "--"
                      ":with-shit"
                      ":with-other-shit")))
    (println "Created app at" full-app-name)))
