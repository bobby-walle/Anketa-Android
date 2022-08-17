package com.example.anketa.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.anketa.R
import com.example.anketa.data.ResourceType
import com.example.anketa.data.Role
import com.example.anketa.model.*
import com.example.anketa.prefs

class CardsViewModel : ViewModel() {

    val personOneImagesId = arrayOf(
        Pair(R.drawable.person_one_1, ResourceType.Drawable),
        Pair(R.raw.person_one_vid, ResourceType.Raw),
        Pair(R.drawable.person_one_2, ResourceType.Drawable),
//        Pair(R.drawable.person_one_3, ResourceType.Drawable),
    )

    val personTwoImagesId = arrayOf(
        Pair(R.drawable.person_two_1, ResourceType.Drawable),
        Pair(R.raw.person_two_vid, ResourceType.Raw),
        Pair(R.drawable.person_two_2, ResourceType.Drawable),
//        Pair(R.drawable.person_two_3, ResourceType.Drawable),
    )

    val personThreeImagesId = arrayOf(
        Pair(R.drawable.person_three_1, ResourceType.Drawable),
        Pair(R.drawable.person_three_2, ResourceType.Drawable),
        Pair(R.drawable.person_three_3, ResourceType.Drawable),
    )

    val restaurantOneResources = arrayOf(
        Pair(R.drawable.restaurant_one_1, ResourceType.Drawable),
        Pair(R.raw.restaraunt_one_vid, ResourceType.Raw),
        Pair(R.drawable.restaurant_one_2, ResourceType.Drawable),
        Pair(R.drawable.restaurant_one_3, ResourceType.Drawable),
//        Pair(R.drawable.restaurant_one_4, ResourceType.Drawable),
    )

    val restaurantTwoResources = arrayOf(
        Pair(R.drawable.restaurant_two_1, ResourceType.Drawable),
        Pair(R.raw.restaraunt_two_vid, ResourceType.Raw),
        Pair(R.drawable.restaurant_two_2, ResourceType.Drawable),
        Pair(R.drawable.restaurant_two_3, ResourceType.Drawable),
//        Pair(R.drawable.restaurant_two_4, ResourceType.Drawable),
    )


    val restaurantThreeImagesId = arrayOf(
        Pair(R.drawable.restaurant_three_1, ResourceType.Drawable),
        Pair(R.drawable.restaurant_three_2, ResourceType.Drawable),
        Pair(R.drawable.restaurant_three_3, ResourceType.Drawable),
        Pair(R.drawable.restaurant_three_4, ResourceType.Drawable),
    )

    val list: ArrayList<BaseModel>
        get() = getListOfModels()


    val listener: SharedPreferences.OnSharedPreferenceChangeListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            when (key) {
                prefs.PREFS_ROLE -> {
                    currentIndex = 0
                    updateCards()
                }
            }
        }

    init {
        prefs.onChangeListener(listener)
    }

    var stream =
        MutableLiveData<TwoCardModel>().apply { TwoCardModel(topCard, bottomCard) }
        private set

    var currentIndex = 0
        private set

    val topCard
        get() = list[currentIndex % list.size]

    val bottomCard
        get() = list[(currentIndex + 1) % list.size]

    private fun updateCards() {
        stream.value = TwoCardModel(
            cardTop = topCard,
            cardBottom = bottomCard,
        )
    }

    fun swipe() {
        currentIndex += 1
        updateCards()
    }

    val employer1 = EmployerModel(
        id = "1",
        name = "Cristal",
        address = "Санкт-Петербург, Загородный проспект, 60",
        position = "Официант",
        salary = "70 000 - 90 000 руб",
        detailTxt = "Нам нужен человек с высоким чувством эмпатии, любящий людей и умеющий подстроиться под клиента. Знание английского языка приветствуется.",
        rating = 4.8F,
        reviewsCount = 245,
        arrayOfResources = restaurantOneResources,
        imgFriendsId = R.drawable.avatars1,
        friendsCount = "74",
        instagramModel = InstagramModel(
            post = 130,
            follower = 320,
            following = 225,
        ),
        arrayOfComments = arrayOf(
            CommentModel(
                R.drawable.avatar1,
                R.drawable.review_stars_5,
                name = "Ольга Прохорова",
                nickname = "@prhrvolga",
                text = "Чуткое руководсво, приятные сотрудники, современное оборудование - все на уровне!"
            ),
            CommentModel(
                R.drawable.avatar2,
                R.drawable.review_stars_4,
                name = "Марина Андропова",
                nickname = "@maryandre",
                text = "Хорошее заведение и персонал, руководство знает толк в ведении бизнеса."
            ),
            CommentModel(
                R.drawable.avatar3,
                R.drawable.review_stars_5,
                name = "Василиса Ковалёва",
                nickname = "@kovasilysa",
                text = "Отличный ресторан, условия работы и график радуют."
            )
        )
    )

    private fun getListOfModels(): ArrayList<BaseModel> = (
            when (prefs.role) {
                Role.Employer -> {
                    arrayListOf(
                        EmployeeModel(
                            id = "1",
                            position = "Официант",
                            name = "Оксана Васильева",
                            address = "Санкт-Петербург",
                            salary = "70 000 - 90 000 руб",
                            experience = "более 5-ти лет",
                            detailTxt = "Увлечена любимым делом, обожаю встречать гостей и длать все, чтобы людям в заведении было комфортно и удобно",
                            reviewsCount = 12,
                            rating = 4.9F,
                            arrayOfResources = personOneImagesId,
                            imgFriendsId = R.drawable.avatars1,
                            instagramModel = InstagramModel(
                                post = 210,
                                follower = 600,
                                following = 435,
                            ),
                            arrayOfComments = arrayOf(
                                CommentModel(
                                    R.drawable.avatar7,
                                    R.drawable.review_stars_5,
                                    name = "Лиля Миронова",
                                    nickname = "@mironova",
                                    text = "Все отлично, очень понравилось! Спасибо Оксане за прекрасное обслуживание!"
                                ),
                                CommentModel(
                                    R.drawable.avatar3,
                                    R.drawable.review_stars_4,
                                    name = "Светлана Назарова",
                                    nickname = "@nsvetik",
                                    text = "В обслуживании все понравилось. Оксана исполнительная, способная, приветливая и дружелюбная с гостями. Единственное, что расстроило - у сотрудницы нет своей униформы, пришлось пойти на дополнительные расходы."
                                ),
                                CommentModel(
                                    R.drawable.avatar8,
                                    R.drawable.review_stars_5,
                                    name = "Виталий Комаров",
                                    nickname = "@vitkomarov",
                                    text = "Нанимал сотрудницу для обслуживания банкета, все прошло на высоте! Гости были довольны. Все понравилось."
                                ),
                            )
                        ),
                        EmployeeModel(
                            id = "2",
                            position = "Повар",
                            name = "Виктор Кузнецов",
                            address = "Санкт-Петербург",
                            salary = "120 000 - 150 000 руб",
                            experience = "более 5-ти лет",
                            detailTxt = "Работаю шеф-поваром последние 7 лет. Кухя и команда - моё все.",
                            reviewsCount = 21,
                            rating = 4.1F,
                            arrayOfResources = personTwoImagesId,
                            imgFriendsId = R.drawable.avatars2,
                            instagramModel = InstagramModel(
                                post = 130,
                                follower = 320,
                                following = 225,
                            ),
                            arrayOfComments = arrayOf(
                                CommentModel(
                                    R.drawable.avatar7,
                                    R.drawable.review_stars_5,
                                    name = "Анна Васильева",
                                    nickname = "@avasilnna",
                                    text = "Виктор больщой профессионал своего дела, его фирменные блюда - отдельное искусство. Исполнительный шеф, опытный руководитель на кухне"
                                ),
                                CommentModel(
                                    R.drawable.avatar3,
                                    R.drawable.review_stars_4,
                                    name = "Валерия Маркова",
                                    nickname = "@valerymrk",
                                    text = "Опытный шеф, опытен в руководстве. За все время работы показал себя хорошим командным лидером."
                                ),
//                                CommentModel(
//                                    R.drawable.avatar8,
//                                    R.drawable.review_stars_5,
//                                    name = "Виталий Комаров",
//                                    nickname = "@vitkomarov",
//                                    text = "Нанимал сотрудницу для обслуживания банкета, все прошло на высоте! Гости были довольны. Все понравилось."
//                                ),
                            )
                        ),
                        EmployeeModel(
                            id = "3",
                            position = "Администратор",
                            name = "Марина Смирнова",
                            address = "Санкт-Петербург",
                            salary = "70 000 - 100 000 руб",
                            experience = "4 года",
                            detailTxt = "Увлечена любимым делом, обожаю встречать гостей и длать все, чтобы людям в заведении было комфортно и удобно",
                            reviewsCount = 12,
                            rating = 5.0F,
                            arrayOfResources = personThreeImagesId,
                            imgFriendsId = R.drawable.avatars3,
                            instagramModel = InstagramModel(
                                post = 330,
                                follower = 1210,
                                following = 725,
                            ),
                            arrayOfComments = arrayOf(
                                CommentModel(
                                    R.drawable.avatar4,
                                    R.drawable.review_stars_5,
                                    name = "Ольга Прохорова",
                                    nickname = "@prhrvolga",
                                    text = "Марина крайне исполнительный сотрудник. Пунктуальность и соблюдения контроля в ресторане - её конёк."
                                ),
                                CommentModel(
                                    R.drawable.avatar2,
                                    R.drawable.review_stars_4,
                                    name = "Марина Андропова",
                                    nickname = "@maryandre",
                                    text = "Марина - внимательный администратор, умеет находить подход как к клиентам, так и к сотрудникам."
                                ),
                                CommentModel(
                                    R.drawable.avatar3,
                                    R.drawable.review_stars_5,
                                    name = "Василиса Ковалёва",
                                    nickname = "@kovasilysa",
                                    text = "Отличный специалист и мастер своего дела"
                                )
                            )
                        ),
                    )
                }
                Role.Employee -> {
                    arrayListOf(
                        EmployerModel(
                            id = "1",
                            name = "Cristal",
                            address = "Санкт-Петербург, Загородный проспект, 60",
                            position = "Официант",
                            salary = "70 000 - 90 000 руб",
                            detailTxt = "Нам нужен человек с высоким чувством эмпатии, любящий людей и умеющий подстроиться под клиента. Знание английского языка приветствуется.",
                            rating = 4.8F,
                            reviewsCount = 245,
                            arrayOfResources = restaurantOneResources,
                            imgFriendsId = R.drawable.avatars1,
                            friendsCount = "74",
                            instagramModel = InstagramModel(
                                post = 130,
                                follower = 320,
                                following = 225,
                            ),
                            arrayOfComments = arrayOf(
                                CommentModel(
                                    R.drawable.avatar1,
                                    R.drawable.review_stars_5,
                                    name = "Ольга Прохорова",
                                    nickname = "@prhrvolga",
                                    text = "Чуткое руководсво, приятные сотрудники, современное оборудование - все на уровне!"
                                ),
                                CommentModel(
                                    R.drawable.avatar2,
                                    R.drawable.review_stars_4,
                                    name = "Марина Андропова",
                                    nickname = "@maryandre",
                                    text = "Хорошее заведение и персонал, руководство знает толк в ведении бизнеса."
                                ),
                                CommentModel(
                                    R.drawable.avatar3,
                                    R.drawable.review_stars_5,
                                    name = "Василиса Ковалёва",
                                    nickname = "@kovasilysa",
                                    text = "Отличный ресторан, условия работы и график радуют."
                                )
                            )
                        ),
                        EmployerModel(
                            id = "2",
                            name = "La Veranda",
                            address = "Санкт-Петербург, наб. р. Фонтанки, 2",
                            position = "Повар",
                            salary = "120 000 - 150 000 руб",
                            detailTxt = "Нам нужен человек с высоким чувством эмпатии, любящий людей и умеющий подстроиться под клиента. Знание английского языка приветствуется.",
                            rating = 4.1F,
                            reviewsCount = 175,
                            arrayOfResources = restaurantTwoResources,
                            imgFriendsId = R.drawable.avatars2,
                            friendsCount = "31",
                            instagramModel = InstagramModel(
                                post = 210,
                                follower = 600,
                                following = 435,
                            ),
                            arrayOfComments = arrayOf(
                                CommentModel(
                                    R.drawable.avatar1,
                                    R.drawable.review_stars_5,
                                    name = "Ольга Прохорова",
                                    nickname = "@prhrvolga",
                                    text = "Чуткое руководсво, приятные сотрудники, современное оборудование - все на уровне!"
                                ),
                                CommentModel(
                                    R.drawable.avatar2,
                                    R.drawable.review_stars_4,
                                    name = "Марина Андропова",
                                    nickname = "@maryandre",
                                    text = "Хорошее заведение и персонал, руководство знает толк в ведении бизнеса."
                                ),
                                CommentModel(
                                    R.drawable.avatar3,
                                    R.drawable.review_stars_5,
                                    name = "Василиса Ковалёва",
                                    nickname = "@kovasilysa",
                                    text = "Отличный ресторан, условия работы и график радуют."
                                )
                            )
                        ),
                        EmployerModel(
                            id = "3",
                            name = "Bubble",
                            address = "Санкт-Петербург, Рижский проспект, 119",
                            position = "Повар",
                            salary = "70 000 - 100 000 руб",
                            detailTxt = "Нам нужен человек с высоким чувством эмпатии, любящий людей и умеющий подстроиться под клиента. Знание английского языка приветствуется.",
                            rating = 5.0F,
                            reviewsCount = 301,
                            arrayOfResources = restaurantThreeImagesId,
                            imgFriendsId = R.drawable.avatars3,
                            friendsCount = "112",
                            instagramModel = InstagramModel(
                                post = 330,
                                follower = 1210,
                                following = 725,
                            ),
                            arrayOfComments = arrayOf(
                                CommentModel(
                                    R.drawable.avatar1,
                                    R.drawable.review_stars_5,
                                    name = "Ольга Прохорова",
                                    nickname = "@prhrvolga",
                                    text = "Чуткое руководсво, приятные сотрудники, современное оборудование - все на уровне!"
                                ),
                                CommentModel(
                                    R.drawable.avatar2,
                                    R.drawable.review_stars_4,
                                    name = "Марина Андропова",
                                    nickname = "@maryandre",
                                    text = "Хорошее заведение и персонал, руководство знает толк в ведении бизнеса."
                                ),
                                CommentModel(
                                    R.drawable.avatar3,
                                    R.drawable.review_stars_5,
                                    name = "Василиса Ковалёва",
                                    nickname = "@kovasilysa",
                                    text = "Отличный ресторан, условия работы и график радуют."
                                )
                            )
                        ),
                    )
                }
            }
            )
}