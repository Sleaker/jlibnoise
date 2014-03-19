# H3 jlibnoise
jlibnoise is a port of libnoise to Java

http://libnoise.sourceforge.net/

Licensed under LGPL.  


libnoise is a portable C++ library that is used to generate [coherent noise](http://libnoise.sourceforge.net/glossary/index.html#coherentnoise "coherent noise"), a type of smoothly-changing noise. libnoise can generate [Perlin Noise](http://libnoise.sourceforge.net/glossary/index.html#perlinnoise "Perlin noise"), ridged multifractal noise, and other types of coherent-noise.

![Mountain.jpg](http://libnoise.sourceforge.net/images/mountain.jpg)

Coherent noise is often used by graphics programmers to generate natural-looking textures, planetary terrain, and other things. The mountain scene shown above was rendered in Terragen with a terrain file generated by libnoise. You can also view some other [examples](http://libnoise.sourceforge.net/examples/index.html "examples") of what libnoise can do.

In libnoise, coherent-noise generators are encapsulated in classes called noise modules. There are many different types of noise modules. Some noise modules can combine or modify the outputs of other noise modules in various ways; you can join these modules together to generate very complex coherent noise.
