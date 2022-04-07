# Welcome Home Android application


### Conventions in development

- Single-activity project with Jetpack Navigation component

- Use Flow for handling threads

- Use clean architecture (use case - repository interface - repository implementation)

- All extended functions should be stored in "ext" package for a certain type, such as ExtString, ExtTime, ExtView etc

- Layouts should support Binding feature (starting with <layout> tag). Kotlin-synthetic using is not allowed!

- We should make builds only from Release branch for incremental increase version number. 
  
- Do not keep all Api interfaces in a single file. Please divide them by their targets such as ApiSession, ApiDwalla, ApiTeleHealth etc.

- In case of opening any Activity provide Router helper, for example
class SampleActivity: BindActivity() {
...

companion object {
        fun open(caller: Any, param1: String? = null, param2: String? = null) =
            Router(
                caller = caller,
                activity = SampleActivity::class.java,
                requestCode = RequestCode.SAMPLE
            )
                .putExtra(Const.Arg.PARAM1, param1)
                .open()
    }

}

- Each Activity and Fragment implement BaseView interface with following methods: createAppBar, createForm, createAdapter. Please do UI initialization inside of them

- Each Fragment / Activity should implement their View interfaces such as MainActivity - MainView. Fragment / Activivites shouldn't contain any private functions inside