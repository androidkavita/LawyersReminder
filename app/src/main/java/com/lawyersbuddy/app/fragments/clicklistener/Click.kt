package com.lawyersbuddy.app.fragments.clicklistener

interface Click {
    fun click(id:String)

}
interface Clickcase {
    fun click(id:Int)

}interface Clientclick {
    fun Clientclick(id:Int)

}interface HearingCLick {
    fun HearingCLick(id:Int)

}
interface caseedit {
    fun clickedit(id:Int)

}
interface ClickIMage {
    fun click(image:String?)

}
interface DeleteImage {
    fun deleteimage(name:String?)

}
interface clickpdf {
    fun clickpdf(image:String?)

}
interface popupItemClickListnerCountry {
    fun getCountry(name: String, flag: String, id: Int)
}
interface deleteaccount{
    fun deleteaccount(id: String?)
}
interface imageclick{
    fun imageclick(position:Int)
}