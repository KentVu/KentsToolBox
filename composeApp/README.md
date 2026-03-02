# TODO app

obeythetestinggoat book's TODO app for KMP.
Let's see what interesting analogies can we discover between a Compose app with a Web app along
the way.

## Chapter 3. Testing a Simple Home Page with Unit Tests

> [...] Django was with any web server, Django’s main job is to decide what to do when a user asks for a particular URL on our site.[...]

→ Need to use Navigation library.

### Unit Testing a View

We can use something like `runComposeUiTest { setContent { <content> } }` instead.
Instead of searching HTML content we can make assertions against the Semantic tree.

→ Does that mean our runComposeUiTest considered Unit test?
  → Maybe yes. 

- Is the title what's inside TopAppBar or the actual desktop's Window title?
  - → Since we're supporting mobile app also, the window's title bar seems irrelevant!

- We can only check the semantic tree, that means we can't further verify whether the text is inside
  the TopAppBar or just in a `Text()` composable!

## TODO

- [x] Setup Cucumber
  - https://www.kodeco.com/26211276-getting-started-with-cucumber
  - https://github.com/realpacific/cucumber-spring-boot-kotlin/blob/main/build.gradle.kts
  - https://github.com/cucumber/cucumber-jvm-starter-gradle-java
- [x] Run the functional test in [Chapter 2](https://www.obeythetestinggoat.com/book/chapter_02_unittest.html).
