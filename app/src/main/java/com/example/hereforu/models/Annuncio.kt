import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
@Parcelize
class Announcement(var uid:String, var announcementId: String, var nome: String, var campo:String, var data: String, var news: String, var description: String, var pubDate: MutableMap<String, String>, var counter:Int, var score: Float):Parcelable {
    constructor() : this("","", "","","","","", HashMap<String, String>(), 0, 0f)
}