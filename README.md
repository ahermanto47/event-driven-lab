# event-driven-lab

argocd app create event-driven-dev --repo https://github.com/ahermanto47/event-driven-lab.git --path environments --dest-server https://api.crc.testing:6443 --dest-namespace event-driven-dev --sync-policy automated