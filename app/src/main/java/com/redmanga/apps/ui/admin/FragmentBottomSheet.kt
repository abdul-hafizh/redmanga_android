package com.redmanga.apps.ui.admin

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Dialog
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.view.ViewStub
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.widget.AppCompatSpinner
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.redmanga.apps.R
import com.redmanga.apps.data.db.entities.Manga
import com.redmanga.apps.data.network.response.Chapter
import com.redmanga.apps.data.network.response.Kategori
import com.redmanga.apps.utils.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import kotlin.properties.Delegates


class FragmentBottomSheet(
    val kategoriList: MutableList<Kategori> = mutableListOf(),
    val layout: Int = 0,
    val type: Int = 0,
    val kategori: Kategori? = null,
    val manga: Manga? = null,
    val dataChapter: Chapter? = null
) : BottomSheetDialogFragment(), KodeinAware {

    override val kodein by kodein()

    private lateinit var adminViewModel: AdminViewModel
    private val factory: AdminViewModelFactory by instance()

    private lateinit var views: View
    private lateinit var mBehavior: BottomSheetBehavior<*>
    private lateinit var appBarLayout: AppBarLayout
    private lateinit var titleBar: TextView
    private lateinit var iconAction: ImageButton
    private lateinit var viewStub: ViewStub
    private lateinit var file: File
    private lateinit var imageUpload: ImageView
    private var idKategori by Delegates.notNull<Int>()

    private var arrayAdapterKategori: ArrayAdapter<String>? = null


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        views = View.inflate(
            context,
            R.layout.fragment_bottom_sheet,
            null
        )
        dialog.setContentView(views)

        adminViewModel = ViewModelProvider(this, factory).get(AdminViewModel::class.java)

        mBehavior = BottomSheetBehavior.from(views.parent as View)
        mBehavior.peekHeight = BottomSheetBehavior.PEEK_HEIGHT_AUTO

        appBarLayout = views.findViewById<View>(R.id.app_bar_layout) as AppBarLayout
        titleBar = views.findViewById(R.id.name_toolbar) as TextView
        iconAction = views.findViewById(R.id.iconAction) as ImageButton
        viewStub = views.findViewById(R.id.viewStub) as ViewStub

        (views.findViewById(R.id.lyt_spacer) as View).minimumHeight =
            Resources.getSystem().displayMetrics.heightPixels / 2

        showView(appBarLayout, getActionBarSize())

        if (type == 0) {
            iconAction.setImageResource(R.drawable.ic_create)
            when (layout) {
                1 -> {
                    viewStub.layoutResource = R.layout.form_tambah_kategori
                    viewStub.inflate()
                    titleBar.text = resources.getString(R.string.tambah_data, "Kategori")
                    setupTambahKategori()
                }
                2 -> {
                    viewStub.layoutResource = R.layout.form_tambah_manga
                    viewStub.inflate()
                    titleBar.text = resources.getString(R.string.tambah_data, "Manga")
                    setupTambahManga()
                }
                3 -> {
                    viewStub.layoutResource = R.layout.form_tambah_chapter
                    viewStub.inflate()
                    titleBar.text = resources.getString(R.string.tambah_data, "Chapter")
                    setupTambahChapter()
                }
                4 -> {
                    viewStub.layoutResource = R.layout.form_tambah_komik
                    viewStub.inflate()
                    titleBar.text = resources.getString(R.string.tambah_data, "Komik")
                    imageUpload = views.findViewById(R.id.upload)

                    imageUpload.setOnClickListener {
                        if (EasyPermissions.hasPermissions(
                                requireContext(),
                                READ_EXTERNAL_STORAGE
                            )
                        ) {
                            showFileChooser()
                        } else {
                            EasyPermissions.requestPermissions(
                                this,
                                "This application need your permission to access photo gallery.",
                                991,
                                READ_EXTERNAL_STORAGE
                            )
                        }
                    }

                    iconAction.setOnClickListener {
                        if (file.exists()) {
                            val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
                            builder.addFormDataPart("id_chapter", dataChapter!!.id_chapter.toString())
                                .addFormDataPart(
                                    "gambar", file.name,
                                    file.asRequestBody(MultipartBody.FORM)
                                )

                            val tambahKomik: RequestBody = builder.build()
                            Coroutines.main {
                                val result = adminViewModel.addKomik(tambahKomik)
                                Toast.makeText(
                                    requireContext(),
                                    result.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Gambar belum dipilih",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        } else {
            iconAction.setImageResource(R.drawable.ic_done)
            when (layout) {
                1 -> {
                    viewStub.layoutResource = R.layout.form_tambah_kategori
                    viewStub.inflate()
                    titleBar.text = resources.getString(R.string.ubah_data, "Kategori")
                    setupEditKategori()
                }
                2 -> {
                    viewStub.layoutResource = R.layout.form_tambah_manga
                    viewStub.inflate()
                    titleBar.text = resources.getString(R.string.ubah_data, "Manga")
                    setupEditManga()
                }
                3 -> {
                    viewStub.layoutResource = R.layout.form_tambah_chapter
                    viewStub.inflate()
                    titleBar.text = resources.getString(R.string.ubah_data, "Chapter")
                    setupEditChapter()
                }
            }
        }

        (views.findViewById(R.id.bt_close) as ImageButton).setOnClickListener { dismiss() }

        return dialog
    }

    override fun onStart() {
        super.onStart()
        mBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun showFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 15)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == -1 && data != null && data.data != null) {
            val convert = data.data
            val pickedImg = getPathFromUri(requireContext(), convert!!)
            file = File(pickedImg!!)
            displayImageOriginal(
                requireContext(),
                imageUpload,
                pickedImg,
                imageUpload.width,
                imageUpload.width * 2
            )
        }
    }

    private fun showView(view: View, size: Int) {
        val params = view.layoutParams
        params.height = size
        view.layoutParams = params
    }

    private fun getActionBarSize(): Int {
        val styledAttributes =
            requireContext().theme.obtainStyledAttributes(intArrayOf(R.attr.actionBarSize))
        return styledAttributes.getDimension(0, 0f).toInt()
    }

    private fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

    private fun setupTambahKategori() {
        val lyNama = views.findViewById(R.id.ly_nama) as TextInputLayout
        val nama = views.findViewById(R.id.nama) as TextInputEditText
        showEditextError(lyNama, nama, "Nama Kategori")
        iconAction.setOnClickListener {
            val namaKategori = nama.text.toString()
            val cek = arrayOfNulls<Boolean>(1)
            cek[0] = showInputError(lyNama, namaKategori, "Nama Kategori")

            if (!cek.contains(false)) {
                Coroutines.main {
                    val result = adminViewModel.addKategori(namaKategori)
                    showToast(requireContext(), result.message)
                    dismiss()
                }
            }
        }
    }



    private fun setupEditKategori() {
        val lyNama = views.findViewById(R.id.ly_nama) as TextInputLayout
        val nama = views.findViewById(R.id.nama) as TextInputEditText
        nama.text = kategori?.nama_kategori?.toEditable()

        showEditextError(lyNama, nama, "Nama Kategori")
        iconAction.setOnClickListener {
            val namaKategori = nama.text.toString()
            val cek = arrayOfNulls<Boolean>(1)
            cek[0] = showInputError(lyNama, namaKategori, "Nama Kategori")

            if (!cek.contains(false)) {
                Coroutines.main {
                    val result =
                        adminViewModel.updateKategori(kategori!!.id_kategori, namaKategori)
                    showToast(requireContext(), result.message)
                    if (result.code == 200) {
                        dismiss()
                    }
                }
            }
        }
    }

    private fun setupTambahManga() {
        val spinnerKategori = views.findViewById(R.id.drop_kategori) as AppCompatSpinner
        val lyTanggal = views.findViewById(R.id.ly_tanggal) as TextInputLayout
        val tanggal = views.findViewById(R.id.tanggal) as TextInputEditText
        val lyJudul = views.findViewById(R.id.ly_judul) as TextInputLayout
        val judul = views.findViewById(R.id.judul) as TextInputEditText
        val lyDeskripsi = views.findViewById(R.id.ly_deskripsi) as TextInputLayout
        val deskripsi = views.findViewById(R.id.deskripsi) as TextInputEditText
        val lyPenulis = views.findViewById(R.id.ly_penulis) as TextInputLayout
        val penulis = views.findViewById(R.id.penulis) as TextInputEditText
        val spinnerStatus = views.findViewById(R.id.drop_status) as AppCompatSpinner

        spinnerStatus.visibility = View.GONE
        imageUpload = views.findViewById(R.id.upload)

        openCalender(context, tanggal)
        showEditextError(lyJudul, judul, "Judul Manga")
        showEditextError(lyTanggal, tanggal, "Tanggal Rilis")
        showEditextError(lyDeskripsi, deskripsi, "Deskripsi")
        showEditextError(lyPenulis, penulis, "Penulis")

        iconAction.setOnClickListener {
            val judu = judul.text.toString()
            val tangga = tanggal.text.toString()
            val deskrips = deskripsi.text.toString()
            val penuli = penulis.text.toString()

            val cek = arrayOfNulls<Boolean>(4)
            cek[0] = showInputError(lyJudul, judu, "Judul")
            cek[1] = showInputError(lyTanggal, tangga, "Tanggal Rilis")
            cek[2] = showInputError(lyDeskripsi, deskrips, "Deskripsi")
            cek[3] = showInputError(lyPenulis, penuli, "Penulis")

            if (file.exists()) {
                val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
                builder.addFormDataPart("id_kategori", idKategori.toString())
                    .addFormDataPart("judul", judu)
                    .addFormDataPart("tgl_release", convertDate(tangga, 3)!!)
                    .addFormDataPart("deskripsi", deskrips)
                    .addFormDataPart("penulis", penuli)
                    .addFormDataPart(
                        "cover", file.name,
                        file.asRequestBody(MultipartBody.FORM)
                    )

                val tambahManga: RequestBody = builder.build()

                if (!cek.contains(false)) {
                    Coroutines.main {
                        val result = adminViewModel.addManga(tambahManga)
                        Toast.makeText(
                            requireContext(),
                            result.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    "Gambar belum dipilih",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        imageUpload.setOnClickListener {
            if (EasyPermissions.hasPermissions(
                    requireContext(),
                    READ_EXTERNAL_STORAGE
                )
            ) {
                showFileChooser()
            } else {
                EasyPermissions.requestPermissions(
                    this,
                    "This application need your permission to access photo gallery.",
                    991,
                    READ_EXTERNAL_STORAGE
                )
            }
        }

        arrayAdapterKategori = ArrayAdapter(requireActivity(),
            android.R
                .layout.simple_spinner_dropdown_item,
            kategoriList.map { it.nama_kategori }
        )

        arrayAdapterKategori!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerKategori.adapter = arrayAdapterKategori
        spinnerKategori.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                idKategori = kategoriList[position].id_kategori
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupEditManga() {
        val txtCover = views.findViewById(R.id.txtCover) as TextView
        val spinnerKategori = views.findViewById(R.id.drop_kategori) as AppCompatSpinner
        val lyTanggal = views.findViewById(R.id.ly_tanggal) as TextInputLayout
        val tanggal = views.findViewById(R.id.tanggal) as TextInputEditText
        val lyJudul = views.findViewById(R.id.ly_judul) as TextInputLayout
        val judul = views.findViewById(R.id.judul) as TextInputEditText
        val lyDeskripsi = views.findViewById(R.id.ly_deskripsi) as TextInputLayout
        val deskripsi = views.findViewById(R.id.deskripsi) as TextInputEditText
        val lyPenulis = views.findViewById(R.id.ly_penulis) as TextInputLayout
        val penulis = views.findViewById(R.id.penulis) as TextInputEditText
        val spinnerStatus = views.findViewById(R.id.drop_status) as AppCompatSpinner

        imageUpload = views.findViewById(R.id.upload)
        imageUpload.visibility = View.GONE
        txtCover.visibility = View.GONE

        requestStatus(requireActivity(), spinnerStatus)
        openCalender(context, tanggal)
        showEditextError(lyJudul, judul, "Judul Manga")
        showEditextError(lyTanggal, tanggal, "Tanggal Rilis")
        showEditextError(lyDeskripsi, deskripsi, "Deskripsi")
        showEditextError(lyPenulis, penulis, "Penulis")

        tanggal.text = convertDate(manga!!.tgl_release, 1)?.toEditable()
        judul.text = manga.judul.toEditable()
        deskripsi.text = manga.deskripsi.toEditable()
        penulis.text = manga.penulis.toEditable()

        iconAction.setOnClickListener {
            val judu = judul.text.toString()
            val tangga = tanggal.text.toString()
            val deskrips = deskripsi.text.toString()
            val penuli = penulis.text.toString()
            val status = spinnerStatus.selectedItem.toString()

            val cek = arrayOfNulls<Boolean>(4)
            cek[0] = showInputError(lyJudul, judu, "Judul")
            cek[1] = showInputError(lyTanggal, tangga, "Tanggal Rilis")
            cek[2] = showInputError(lyDeskripsi, deskrips, "Deskripsi")
            cek[3] = showInputError(lyPenulis, penuli, "Penulis")

            if (!cek.contains(false)) {
                Coroutines.main {
                    val result = adminViewModel.updateManga(
                        manga.id_manga.toString(),
                        idKategori.toString(),
                        judu,
                        convertDate(tangga, 3)!!,
                        deskrips,
                        penuli,
                        status
                    )
                    Toast.makeText(
                        requireContext(),
                        result.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    if (result.code == 200) {
                        dismiss()
                    }
                }
            }
        }

        arrayAdapterKategori = ArrayAdapter(requireActivity(),
            android.R
                .layout.simple_spinner_dropdown_item,
            kategoriList.map { it.nama_kategori }
        )

        arrayAdapterKategori!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerKategori.adapter = arrayAdapterKategori
        spinnerKategori.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                idKategori = kategoriList[position].id_kategori
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        spinnerKategori.setSelection(kategoriList.map { it.id_kategori }
            .indexOf(manga.id_kategori))
    }

    private fun setupTambahChapter() {
        val lyChapter = views.findViewById(R.id.ly_chapter) as TextInputLayout
        val chapter = views.findViewById(R.id.chapter) as TextInputEditText
        val lyJudul = views.findViewById(R.id.ly_judul) as TextInputLayout
        val judul = views.findViewById(R.id.judul) as TextInputEditText

        showEditextError(lyChapter, chapter, "Chapter")
        showEditextError(lyJudul, judul, "Judul Chapter")

        iconAction.setOnClickListener {
            val chapte = chapter.text.toString()
            val judu = judul.text.toString()

            val cek = arrayOfNulls<Boolean>(2)
            cek[0] = showInputError(lyChapter, chapte, "Chapter")
            cek[1] = showInputError(lyJudul, judu, "Judul Chapter")

            if (!cek.contains(false)) {
                Coroutines.main {
                    val result = adminViewModel.addChapter(
                        manga?.id_manga.toString(), chapte, judu,
                        getDateNow()!!
                    )
                    showToast(requireContext(), result.message)
                    dismiss()
                }
            }
        }
    }

    private fun setupEditChapter() {
        val lyChapter = views.findViewById(R.id.ly_chapter) as TextInputLayout
        val chapter = views.findViewById(R.id.chapter) as TextInputEditText
        val lyJudul = views.findViewById(R.id.ly_judul) as TextInputLayout
        val judul = views.findViewById(R.id.judul) as TextInputEditText

        showEditextError(lyChapter, chapter, "Chapter")
        showEditextError(lyJudul, judul, "Judul Chapter")

        chapter.text = dataChapter?.chapter.toString().toEditable()
        judul.text = dataChapter?.judul_chapter?.toEditable()
        iconAction.setOnClickListener {
            val chapte = chapter.text.toString()
            val judu = judul.text.toString()

            val cek = arrayOfNulls<Boolean>(2)
            cek[0] = showInputError(lyChapter, chapte, "Chapter")
            cek[1] = showInputError(lyJudul, judu, "Judul Chapter")

            if (!cek.contains(false)) {
                Coroutines.main {
                    val result =
                        adminViewModel.updateChapter(
                            dataChapter?.id_chapter.toString(),
                            manga?.id_manga.toString(),
                            chapte,
                            judu
                        )
                    showToast(requireContext(), result.message)
                    if (result.code == 200) {
                        dismiss()
                    }
                }
            }
        }
    }
}