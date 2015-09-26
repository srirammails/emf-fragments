#This is a list of the more complicated design decisions and their discussion

# Design Decisions #

### How to produce a context for new user objects? ###

When a user created a new object using the generated factories, EMF-Fragments cannot know to which model the object is eventually going to be a part of. The information about which model a new object is intended for is called the "context" of this object.

There are at least there potential strategies to deal with this problem:
  1. Users have to assign a context to each new object before the object is used.
  1. Users have to add the object to something that already has a context before the object is used otherwise.
  1. EMF-Fragments postpones all actions that require a context until the object gets a context.
  1. The context is set globally by the user. (This is done right now)
  1. EMF-Fragments somehow extends the generated factories so that a context is given during creation.
  1. All new objects are held in a pseudo context. (This is what we want in the future)

There a merits and flaws to each strategy:
  1. This is tedious for the user, but easy to implement. It also causes problems for generated editor, probably for other 3rd-party tools too.
  1. This is a limitation. It might be ok for the user, but can cause problems with 3rd-party tools.
  1. This is extremely difficult to implement.
  1. This allows only one model at a time. It is easy to implements and (as long as there is only one model) also good for the use or 3rd-party products.
  1. Not sure if this is possible (intended by EMF) and probably causes troubles to the use of 3rd-party tools.
  1. **(implemented)** Only flaw: Users can only add something to a fragmenting reference when the container was added to the model and cross references are not managed as long as they are not part of the model.