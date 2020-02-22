[![YAPION Version V2.0](https://img.shields.io/badge/YAPION%20Version-2.0-red)](https://github.com/yoyosource/YAPI/tree/master/src/main/java/yapi/manager/yapion)
[![Java Version V12.0.1](https://img.shields.io/badge/Java%20Version-12.0.1-blue.svg)](https://github.com/yoyosource/YAPI/tree/master/src/main/java/yapi/manager/yapion)
[![License: Apache 2.0](https://img.shields.io/badge/license-Apache%202-blue)](http://www.apache.org/licenses/LICENSE-2.0)

# YAPION
**YAPION** (jap*i*on) is a slim and fast Object Notation specialized on Java Objects.
It can handle recursive Object structures and state specific serialization and deserialization (SsS/D).
This SsS/D is achieved by a self build annotation system used by YAPION. This object notation is designed for easy usage with complex Object structures and can do even more complex stuff fairly easy.   

## YAPION's naming
```
Y   -> @yoyosource   
API -> application programming interface
ON  -> object notation
```

## Structure
YAPION is heavily inspired by JSON and you can see some similarities between those object notations.
Both have objects as a key, value map and arrays. An YAPION structure can either be an object or an array. An object starts with '{' and ends with '}'. Complementary an array starts with '\[' ends with ']'.
YAPION objects and arrays can be converted to JSON and back without any efforts. For this there are 'toJSON()' and 'toYAPION()' methods in the respective classes.
The String representation of an YAPION Object or Array is mostly shorter than the JSON equivalent. This is achieved by eliminating most quotation marks and building the key value pairs in a enum like fashion.
YAPION as a object notation has 4 major types. Those are value, array, object and pointer (see: Pointer).
Values are indicated by '(...)', arrays by '\[...]', objects by '{...}' and pointers by '->...', the 3 dots are representing the value or the key value pairs.
### Objects, Key-Value pairs  
```
Example for an empty YAPION object
JSON:   {}
YAPION: {}
Example for an YAPION object with "name":"yoyosource" as value pair
JSON:   {"name":"yoyosource"}
YAPION: {name(yoyosource)}
Example for an YAPION object with "owner":true as value pair
JSON:   {"owner":true}
YAPION: {owner(true)}
Example for an YAPION object with "owner":"true" as value pair
JSON:   {"owner":"true"}
YAPION: {owner("true")}
Example for an YAPION object with "price":10 as value pair
JSON:   {"price":10}
YAPION: {price(10)}
Example for an YAPION object with "   price":10 as value pair
JSON:   {"   price":10}
YAPION: {\   price(10)}
Example for an YAPION object with "name":"yoyosource" and "owner":true as value pairs
JSON:   {"name":"yoyosource","owner":"true"}
YAPION: {name(yoyosource)owner(true)}
```
### Arrays
```
Example for an empty YAPION array
JSON:   []
YAPION: []
Example for an YAPION array with numbers
JSON:   [0,1,2,3,4,5,6,7,8,9,10]
YAPION: [0,1,2,3,4,5,6,7,8,9,10]
Example for an YAPION array with values
JSON:   [true,false,null,"Hello"]
YAPION: [true,false,null,Hello]
Example for an YAPION array with objects
JSON:   [{"name":"yoyosource","owner":true},{"name":"chaoscaot444","owner":"false"}]
YAPION: [{name(yoyosource),owner(true)},{name(chaoscaot444)owner(false)}]
Example for embedding an YAPION array in another object.
JSON:   {"contributor":[{"name":"yoyosource","owner":true},{"name":"chaoscaot444","owner":"false"}]}
YAPION: {contributor[{name(yoyosource),owner(true)},{name(chaoscaot444)owner(false)}]}
```
### Pointer
Pointers are in YAPION for serialization and deserialization of recursive structures which JSON lacks. All examples now can only exist in YAPION and cannot be converted to JSON.
Please have this in mind if you are trying to serialize a bigger Java object with recursive object structure. Trying to convert this to JSON will throw an 'YAPIONException' or an 'YAPIONConversionException'.
All pointer shown in here are valid pointer but are mostly meaningless because a pointer gets constructed by your current object state.
IMPORTANT: You could represent YAPION pointer as a Key-Value pair with string as value but I won't do this for now because I cannot be sure to have a pointer or just a string on the conversion back to YAPION.
```
Example for an YAPION pointer
{hello->l6J8F3AHgGhBh6aIC1AEHG3D1AHfH9igDAIeC7aLdFj9Kb0A89EGclhG9fDkhJbH7LDiKglB69ihaCGGDHe1eA98ikg9D72Bci90FiJ8A0KdChgI1lbKddLIjghE1C1k}
These pointer point to another YAPION Object in the same serialization. For reconstructing the recursion this pointer points to the object it is reffering to.
A pointer is not a hash and is not intended to be secure. If 2 objects in your tree have the same pointer it will point to the first by default. The YAPION serializer will put '*<CHARACTER>' before the pointer to indicate which specific object it is reffering to.

Example for an YAPION pointer. Referencing the 8th Object with this reference.
{hello->*al6J8F3AHgGhBh6aIC1AEHG3D1AHfH9igDAIeC7aLdFj9Kb0A89EGclhG9fDkhJbH7LDiKglB69ihaCGGDHe1eA98ikg9D72Bci90FiJ8A0KdChgI1lbKddLIjghE1C1k}
This pointer starts with '*a', this indicates the deserializer that this should pointer to the 8th Object with this reference.
The list of prefixes for pointer is as follows:
01236789abcdefghijklABCDEFGHIJKL
Have in mind that the '*0' is the same as just having the pointer without the star notation.

These to pointer reference the same Object in the tree. The second one is just 2 characters longer because of the '*0'
{hello->l6J8F3AHgGhBh6aIC1AEHG3D1AHfH9igDAIeC7aLdFj9Kb0A89EGclhG9fDkhJbH7LDiKglB69ihaCGGDHe1eA98ikg9D72Bci90FiJ8A0KdChgI1lbKddLIjghE1C1k} 
{hello->*0l6J8F3AHgGhBh6aIC1AEHG3D1AHfH9igDAIeC7aLdFj9Kb0A89EGclhG9fDkhJbH7LDiKglB69ihaCGGDHe1eA98ikg9D72Bci90FiJ8A0KdChgI1lbKddLIjghE1C1k}
```