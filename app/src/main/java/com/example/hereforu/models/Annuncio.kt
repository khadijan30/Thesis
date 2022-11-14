import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
@Parcelize
class Announcement(var uid:String, var announcementId: String, var nome: String, var campo:String, var data: Long, var news: String, var description: String, var pubDate: MutableMap<String, String>, var counter:String, var score: String):Parcelable {
    constructor() : this("","", "","",0,"","", HashMap<String, String>(), "0", "0")
}