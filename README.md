# Http4s-request-printer

Super tiny Http4s server for inspecting requests.
Useful if you want to see what your request looks like "on the other side" from some running environment.

- Formats request information as json
- Logs all requests and generated response
- Responds 200 with attached json description of request


#

Run with `sbt '~reStart'`

Make docker image with `sbt docker`

Push docker image with `sbt dockerPush`