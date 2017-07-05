# Google OAuth plugin for GoCD

The plugin allows user to login in GoCD using a google account. It is implemented using [GoCD authorization endpoint](https://plugin-api.gocd.org/current/authorization/). The older [Google Oauth Plugin](https://github.com/gocd-contrib/gocd-oauth-login/tree/master/google) was implemented using `GoCD authentication endpoint`, which was deprecated in [GoCD release 17.5.0](https://www.gocd.org/releases/#17.5.0). Hence, we have provided this plugin as its replacement. 

# Installation

Installation documentation available [here](INSTALL.md).

# Capabilities

* Currently supports only authentication capability, authorization is TBD.

## Building the code base

To build the jar, run `./gradlew clean test assemble`

## License

```plain
Copyright 2017 ThoughtWorks, Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
