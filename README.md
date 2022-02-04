# event-driven-lab

argocd app create event-driven-dev --repo https://github.com/ahermanto47/event-driven-lab.git --path camelk --dest-server https://api.crc.testing:6443 --dest-namespace event-driven-dev --sync-policy automated

# manual setup

oc apply -f operator-subscriptions

oc apply -f crds\knative-crd.yaml

oc new-project event-driven-dev

oc apply -f crds\integration-crd.yaml

oc process openshift//postgresql-ephemeral | oc apply -f -

oc create secret generic postgresql-datasource --from-file=camelk\datasource.properties

oc apply -f camelk\rest-to-postgresql.yaml

# postgresql

oc exec --stdin --tty postgresql-1-lhrd9 -- /bin/bash

psql -U userKPU sampledb

CREATE TABLE sfdc_contact (
    id VARCHAR PRIMARY KEY,
    account_id VARCHAR,
    first_name VARCHAR,
    last_name VARCHAR,
    phone VARCHAR,
    email VARCHAR
    
);

# Curl in Linux

curl -k --header "Content-Type: application/json" --request POST --data '{"Id":"0123", "AccountId":"XYZ987", "FirstName":"George", "LastName": "Washington", "Email":"george.washington@whitehouse.gov", "Phone":"(888) 222-3333"}' https://rest-to-postgresql-event-driven-dev.apps-crc.testing/contact/

# Curl in Windows

curl -k -H "Content-Type: application/json" -d "{\"Id\":\"0123\",\"AccountId\":\"XYZ987\", \"FirstName\":\"George\", \"LastName\":\"Washington\", \"Email\":\"george.washington@whitehouse.gov\", \"Phone\":\"(888) 222-3333\"}" -X POST https://rest-to-postgresql-event-driven-dev.apps-crc.testing/contact/

# troubleshooting

oc logs camel-k-operator-77b7476568-bnzsh -n openshift-operators

oc logs pod/rest-to-postgresql-00001-deployment-7f46889f8d-fk4hq -c integration