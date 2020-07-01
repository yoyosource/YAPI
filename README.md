[![YAPI Release V2.2.0](https://img.shields.io/badge/YAPI%20Release-2.3.0-green)](https://github.com/yoyosource/YAPI/releases/tag/v1.2)
[![YAPI Pre-Release V?.?-alpha](https://img.shields.io/badge/YAPI%20Pre--Release-%3F.%3F--alpha-yellow)](https://github.com/yoyosource/YAPI/releases/tag/v1.2)
[![YAPI Version V2.2.0](https://img.shields.io/badge/YAPI%20Version-2.3.0-red)](https://github.com/yoyosource/YAPI/releases/tag/v1.2)
[![YAPION Version V2.0](https://img.shields.io/badge/YAPION%20Version-2.0-red)](https://github.com/yoyosource/YAPI/tree/master/src/main/java/yapi/manager/yapion)

[![Files 305](https://img.shields.io/badge/Files-305-inactive)](https://github.com/yoyosource/YAPI)
[![Methods 1642](https://img.shields.io/badge/Methods-1642-inactive)](https://github.com/yoyosource/YAPI)
[![Code Lines 31533](https://img.shields.io/badge/Code%20Lines-31533-inactive)](https://github.com/yoyosource/YAPI)

[![Java Version V12.0.1](https://img.shields.io/badge/Java%20Version-12.0.1-blue.svg)](https://github.com/yoyosource/YAPI/releases/tag/v1.0)
[![License: Apache 2.0](https://img.shields.io/badge/license-Apache%202-blue)](http://www.apache.org/licenses/LICENSE-2.0)

# YAPI
My Java API **YAPI** is a diverse and powerful API to make your live easier with coding. Some problems and ideas have neat solutions within this api. To use it just import it and for questions ask me on GitHub. Feel free to leave suggestions and improvements to any method, class or structure there too. 

## YAPI's naming
Y for YoyoNow or @YoyoSource. API for application programming interface. You could even say library as this is a consortium of methods and data structures.

## License

YAPI is licensed under the terms of the Apache License 2.0.
See the file LICENSE or visit [http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0) for details.

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

## Using in other Projects
YAPI is not published on jcenter or mavenCentral. It is published directly to this GitHub repository. You can declare it as a dependency in your gradle.build with:
```Groovy
repositories {
    maven {
        url = uri("https://raw.githubusercontent.com/yoyosource/YAPI/master/releases")
    }
}

dependencies {
    implementation 'yoyosource:YAPI:2.3.0'
}
```
Alternative you can download the jar directly from [releases](https://github.com/yoyosource/YAPI/releases). 

# APIs used
- eobermuhlner/big-math (https://github.com/eobermuhlner/big-math/tree/master)
  - [V] 2.3.0
  - [L] MIT
- fusesource/jansi (https://github.com/fusesource/jansi/tree/master)
  - [V] 1.18
  - [L] Apache-2.0
- profesorfalken/jProcesses (https://github.com/profesorfalken/jProcesses)
  - [V] 1.6.4
  - [L] Apache-2.0

* [V] Version
* [L] Licence
  
## Goals
| Goal    | Type       | Reach Date  |
| ------: | :--------: | ----------: |
|  20,000 | Code Lines | 14 Feb 2020 |
|   1,000 | Methods    | 14 Feb 2020 |
|   1,500 | Methods    | 29 Apr 2020 |
|  40,000 | Code Lines |           ? |
|   2,000 | Methods    |           ? |
|  60,000 | Code Lines |           ? |
|   4,000 | Methods    |           ? |
|  80,000 | Code Lines |           ? |
|   5,000 | Methods    |           ? |
| 100,000 | Code Lines |           ? |

## Version Log
- `1.0` [R] `06 Nov 2019` > Focus on Math, Encryption, and Password cracking
- `1.1` [A] `21 Jan 2020` > Pre-Release of **1.2**. Focus on String Manipulation and Compression
- `1.2` [R] `05 May 2020` > Focus on Console (ANSI) and log system.
- `2.0.0` [R] `02 Jun 2020` > Mayor changes throughout the YAPI.  

* [R] Release
* [A] Alpha
* [B] Beta
* [C] Coming soon
* [P] Planned

### Version 1.0
Version 1.0 is the initial release of the [YAPI](https://github.com/yoyosource/YAPI). This Version focuses on Math, Encryption and Password cracking through brute-force. As the [YAPI](https://github.com/yoyosource/YAPI) is just a Hobby project the releases won't be regular and what gets released will be heavily influenced by my mood.
I will although try to remove any [Issue](https://github.com/yoyosource/YAPI/issues) and [Bug](https://github.com/yoyosource/YAPI/issues) as soon as possible. [Enhancements](https://github.com/yoyosource/YAPI/issues) are encouraged but will be worked off `.dequeue()` after [Bugs](https://github.com/yoyosource/YAPI/issues) or [Issues](https://github.com/yoyosource/YAPI/issues). As I am alone I prefer that you can help me with a [pull request](https://github.com/yoyosource/YAPI/pulls) on GitHub. I will look into any and try to implement your idea or pull request with the existing system if it is useful and possible.
The [Wiki](https://github.com/yoyosource/YAPI/wiki) will be done soon. If you want to help me fell free to do so with ideas on how to create the Wiki. 
### Version 1.1-alpha
Version 1.1-alpha is a pre-release of the [YAPI](https://github.com/yoyosource/YAPI) and is unfinished. There shouldn't be any problems with this version and everything should work alright. The focus of this version was on some String-Utilities, an efficient Object-Notation (YAPION) and a Compression algorithm for this Object-Notation. 
I also want to thank everyone, who participated in helping me with this project. With every tester I had, the YAPI got a bit better and I am pleased with this. I hope you all will help even more. To give me an Idea or Bug-Report just make a [pull request](https://github.com/yoyosource/YAPI/pulls) or open an [issue](https://github.com/yoyosource/YAPI/issues) on GitHub.
### Version 1.2
Version 1.2 is the third mayor release of the [YAPI](https://github.com/yoyosource/YAPI) with many new features and utilities. The focus of this release was on a Console system with ANSI support, a log system. As well as FileUtils, such as HexFileInputStream and HexFileOutputStream.
I also want to thank everyone who helped me and tested the YAPI. If you want to help me create a [pull request](https://github.com/yoyosource/YAPI/pulls) or report a bug with an [issue](https://github.com/yoyosource/YAPI/issues) on GitHub.
### Version 2.0.0
Version 2.0.0 is a mayor release and refit of the previous versions. Many Utilities got added as well as new Math stuff, String stuff and more.

## Maven Version Log
### Version 1.0
### Version 1.1
### Version 1.2
- `22 Feb 2020` `1.2`
- `18 Mar 2020` `1.2-1`
- `18 Mar 2020` `1.2-2`
- `11 Apr 2020` `1.2-3`
- `11 Apr 2020` `1.2-3.1`
- `11 Apr 2020` `1.2-3.2`
- `11 Apr 2020` `1.2-3.3`
- `11 Apr 2020` `1.2-3.4`
### Version 1.3
- `05 May 2020` `1.3`
- `05 May 2020` `1.3-1`
- `05 May 2020` `1.3-1.1`
- `05 May 2020` `1.3-1.2`
- `05 May 2020` `1.3-1.3`
Changed version Naming to `[VERSION]-[REBUILD]`, eg. `1.3-5` (Version: 1.3, Rebuild: 5)
- `13 May 2020` `1.3-5`
- `14 May 2020` `1.3-6`
- `14 May 2020` `1.3-7`
- `15 May 2020` `1.3-8`
- `15 May 2020` `1.3-9`
- `17 May 2020` `1.3-10`
- `17 May 2020` `1.3-11`
### Version 2
Changed version Naming to `[MAYOR].[MINOR].[PATCH]`, eg. `2.0.0` (Mayor: 2, Minor: 0, Patch: 0)
- `02 Jun 2020` `2.0.0`
- `02 Jun 2020` `2.0.1`
- `03 Jun 2020` `2.0.2`
- `03 Jun 2020` `2.1.0`
- `23 Jun 2020` `2.2.0`
- `01 Jul 2020` `2.2.1`
- `01 Jul 2020` `2.2.2`
- `01 Jul 2020` `2.3.0`
