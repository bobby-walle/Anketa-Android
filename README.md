# Questionnaire
## Стек
- MVVM
- Single Activity
- Navigation Components
- LiveData

### Данные с работодателями/работниками
Данные о работодателях/работниках инициализируются в классе ```CardsViewModel``` пакета ```viewmodel```. 
Функция ```getListOfModels()``` возвращает список моделей ```EmployeeModel``` или ```EmployerModel``` в зависимости от роли, выбранной при входе в приложение.
Выбранная пользователем роль хранится в ```SharedPreferences```. Для работы с ```SharedPreferences``` используется синглтон объект класса ```Prefs``` из пакета ```data```. 
Модели ```EmployeeModel``` и ```EmployerModel``` наследуются от абстрактного класса ```BaseModel```. Модели содержат в себе данные о пользователе, массив пар с id ресурсов видео и изображений, модель с данными из инстаграмма и массив с моделями комментариев для ```DetailFragment```. Для добавления нового работодателя/работника необходимо в формирующийся в функции ```getListOfModels()``` список моеделей добавить модель с данными для соотвествующего работодателя/работника.
Модель ```employer1``` вынесена в отдельное свойство, т.к. ее данные используется в качестве mock'a в  ```ProfileEmployerFragment ```.

### Главный экран/экран свайпов
Анимация свайпов реализвана при помощи MotionLayout.
В ```res/xml/fragment_main_scene``` описаны состояния свайпов.
Карточки представляют собой MaterialCardView. В разметке созданы три карты, cardOne, cardTwo - функциональные и для отображения данных, cardThree - только для создания эффекта выдвигающейся вниз на фоне карты в момент свайпа.
Для создания эффекта карусели, т.е. прокрутки карт по-кругу(в демо-варианте 3 карты), в ```CardsViewModel``` имеются свойства ```topCard``` и ```bottomCard```. LivaData cвойство ```stream``` в ```CardsViewModel``` использутся в качестве observable объекта для обновления состояния UI. В функции  ```setOnTransitionCompleted()``` фрагмента  ```MainFragment ```, устанавливается логика, которая срабатывает в моменты окончания свайпов. Метод  ```swipe()``` из  ```CardsViewModel``` обновляет  ```topCard``` и  ```bottomCard``` и значение   ```stream```. В ```MainFragment``` в функции ```bindCard()``` используется задержка между функциями  ```updateAdaptersBeforeSwipe()```, ```bindBottomCard()``` это необходимо, чтобы устранить эффект мерцания в момент окончания свайпа, т.е. в момент обновления данных в cardOne(topCard) и cardTwo(bottomCard).

## FragmentList и почему RecyclerView вместо ViewPager для изображений/видео. 
В текущей версии для показа изображений и видео используется ```RecyclerView + Snaphelper(PagerSnapHelper) + TabLayout```.
Изначально был реализован вариант с использованием  ```ViewPager2 + TabLayout```, но при показе карт в режиме списка во фрагменте ```ListFragment```, возникала проблема с пользовательским инпутом - свайпы по ```ViewPager2``` обрабатывались только при крайне строгом дивжении по горизонтальной оси. Большой палец двигается в изогнутом/круговом направлении, создавая движение как по оси X, так и по оси Y, и поэтому rootRecyclerView перехватывает смахивание, создавая неприятный пользовательский опыт. По этой причине ```ViewPager2``` был заменен на связку ```RecyclerView + Snaphelper(PagerSnapHelper) + TabLayout```. Так же для решения этой проблемы было насписано несколько функций-расширений для RecyclerView, которые находятся в классе ```SnapHelperTools``` пакета ```tools```.
В ```ViewPager2``` имеется возможность получения текущего фокуса с помощью ```getCurrentItem()```, из-за замены ```ViewPager2``` на ```RecyclerView```, этот метод отуствует. Поэтому была реализована версия, детали реализации которой описаны по второй ссылке снизу. 

- [Create ViewPager using RecyclerView](https://www.linkedin.com/pulse/create-viewpager-using-recyclerview-android-ali-ahmed/)
- [Detecting snap changes with Android’s RecyclerView SnapHelper](https://medium.com/over-engineering/detecting-snap-changes-with-androids-recyclerview-snaphelper-9e9f5e95c424)

## ProfileEmployerFragment, VacancyAdapter
В фрагменте профиля работодателя имеется ```RecyclerView```, для отображения данных с вакансиями. В качестве mock'ов в классе адаптеров создается список моделей типа ```Vacancy```, которые отображаются в ```RecyclerView```

## MainActivity
В функции ```setupNavBottomDestinations()``` устанавливается слушатель назначений для панели навигации NavigationBottom.
Изначально в ```res/menu/bottom_nav_menu``` назначение профиля установлено на профиль для работодателя ```navigation_profile_employer```. Слушатель при нажатии на элемент ```bottom_nav_menu``` профиля(иконка человека) проверяет роль, которую выбрал порльзователь, и открывает фрагмент соответсвующей роли. Т.к назначение ```navigation_edit_profile_employee``` не соотвествует элементу ```bottom_nav_menu``` в ```res/menu/bottom_nav_menu```, необходимо вручную установить элемент профиля активным. То же самое выполняется для ```FragmentList``` и соответсвующему ему назначению ```navigation_list```.

## ResponseFragment 
Функция0 ```getModels()``` формирует и возвращает список моделей, id которых хранятся в ```SharedPreferences```.
При нажатии кнопки "Откликнуться" во фрагментах ```MainFragment```, ```ListFragment```, ```DetailFragment```, в ```SharedPreferences``` при помощи функции ```addResponse()``` заносится id той модели, на которую откликнулись. 
В ```ResponseFragment```, в адаптер ```ResponseAdapter``` передается список, сформированный функцией ```getModels()```.


