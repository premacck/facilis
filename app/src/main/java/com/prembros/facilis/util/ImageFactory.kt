package com.prembros.facilis.util

import java.util.*

private val imageList = arrayListOf(
        "https://miro.medium.com/max/854/1*WgROsTKa6diRYTG5K0R5mw.jpeg",
        "http://newnation.sg/wp-content/uploads/random-pic-internet-22.jpg",
        "https://i.redd.it/5uyrc8opy9uy.jpg",
        "http://cdn.ebaumsworld.com/mediaFiles/picture/1969759/85211517.jpg",
        "https://vignette.wikia.nocookie.net/blogclan-2/images/b/b9/Random-image-15.jpg/revision/latest?cb=20160706220047",
        "http://www.funcage.com/blog/wp-content/uploads/2013/11/Random-Photoshopped-Pictures-006.jpg",
        "https://i.pinimg.com/236x/06/5f/e4/065fe46d6438c7c15c8b8a0b6e760e9f.jpg",
        "https://i.pinimg.com/originals/9d/94/62/9d9462b5c125156f7628d460aa6256ac.jpg",
        "https://i.pinimg.com/originals/2a/c1/19/2ac1192db9d4ed59dd0aa9e4d20d79e2.jpg",
        "https://i.pinimg.com/originals/86/84/e9/8684e9f4786b69053bb59129475dfbc9.jpg"
)

fun getRandomImageUrl() = imageList[Random().nextInt(imageList.size - 1)]