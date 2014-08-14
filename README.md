# **Shazbat**

A simple java program that recreates concurrency and synchronization issues (bogons) to provide
an opportunity to use the tools available within the JRE, JDK and using system tools to practice
at hunting them down and learning to recognize them through gaining practical experience.

There is a wealth of material on concurrent programming.

There is relatively little on hunting down concurrency issues.

## Origins

I've written tools like this before to help others or written testcases to attempt recreating
concurrency issues in production systems myself. Most of us have. What I have failed to do
is catalogue them in their own right.

Some concurrency issues, such as classic deadlock is easy to detect with many tools. Other
issues manifest in more subtle ways (only affect certain platforms, or certain runtimes, or
certain runtimes with certain GC/VM configurations for example).

## Getting started

Build it by invoking ant

```bash
$ ant
```

Run it by invoking java. For example, to get usage and list bogons

#### Example.

Usage and list available _'bogons'_

```bash
$ java -jar shazbat.jar
usage:

    java -cp /path/to/shazbat.jar shazbat.NanuNanu <name> 
    java -jar shazbat.jar <name> 
    java -jar shazbat.jar <name> 

  where <name> is one of:

    SHAZBAT.BOGONS.TRIVIALDEADLOCK
    SHAZBAT.BOGONS.RIPVANWINKLE
    SHAZBAT.BOGONS.BURNCPU

  then good hunting!
$ 
```

#### Example. 

Burn CPU!

```bash
$ java -jar shazbat.jar SHAZBAT.BOGONS.BURNCPU
```

If you check your processor utilization (eg: top, htop) you should see they're all at or close to 100% utilized. You should easily be able to verify that shazbat is the culprit. If you run this on a production or monitored environment, alarms and triggers should be tripped in monitoring systems.

## Contribute

Please fork this repository and submit a PR with bogons of your own to grow the catalogue.

Its simple to contribute. Just add a class that implements the Bogon interface with something nasty and juicy to exercise hunting and debugging tools and skills with.

## Acknolwdgements

Nitsan Wakart, for prodding me to recreate this tool.

## License

MIT.
 
