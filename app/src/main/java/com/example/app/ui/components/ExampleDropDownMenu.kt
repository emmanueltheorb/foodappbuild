package com.example.app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app.R
import com.example.app.data.OptionState
import com.example.app.data.OptionStateViewModel
import com.example.app.data.OptionStateViewModel.OptionStateInput
import com.example.app.ui.components.sections.BoxForCheckButton
import com.example.app.ui.components.sections.ButtonRow
import com.example.app.ui.components.sections.OptionColumn
import com.example.app.ui.components.sections.OptionsInMultiSelectionList
import com.example.app.ui.components.sections.OptionsPreviewSection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.collections.forEach
import kotlin.collections.plus
import kotlin.collections.set

//
//@Composable
//fun OptionModeSwitch(
//    modifier: Modifier = Modifier,
//    optionStateViewModel: OptionStateViewModel = viewModel()
//) {
//    val state = rememberMultiSelectionState()
//    val optionInputs = optionStateViewModel.optionInputs.collectAsState()
//    val optionList by optionStateViewModel.optionStates.collectAsState()
//    var signal by remember { mutableIntStateOf(1) }
//    var signalForPopUp by remember { mutableIntStateOf(1) }
//    var signalForAddIcon by remember { mutableStateOf(false) }
//    val onAddClicked = {
//        signal = 1
//    }
//    val onTickClicked = {
//        signal = 2
//    }
//    val onRightClicked = {
//        signal = 3
//    }
//    val onEditClicked = {
//        signal = 2
//    }
//    val onIncreaseButtonClicked: () -> Unit = {
//        optionStateViewModel.addNewOptionInput()
//    }
//    val onDecreaseButtonClicked: () -> Unit = {
//        optionStateViewModel.removeLastOptionInput()
//    }
//    val selectedItems = remember {
//        mutableStateListOf<OptionState>()
//    }
//    val selectedItemsInBox = remember {
//        mutableStateListOf<OptionState>()
//    }
//    val mergedItemsContainers by optionStateViewModel.mergedItemsContainers.collectAsState()
//    val filteredItems by optionStateViewModel.filteredItems.collectAsState()
//    var mergeGroupNumber by remember { mutableIntStateOf(0) }
//    val onSelectedItems: (List<OptionState>) -> Unit = {
//        if (it.size >= 2 || selectedItemsInBox.isNotEmpty()) {
//            signalForPopUp = 2
//        } else {
//            signalForPopUp = 1
//        }
//        if (it.isNotEmpty()) {
//            signalForAddIcon = true
//        }
//    }
//    val onSelectedItemsInBox: (List<OptionState>) -> Unit = {
//
//    }
//    val onMergeClicked: () -> Unit = {
//        optionStateViewModel.addToMergeGroup(mergeGroupNumber, selectedItems)
//        selectedItems.clear()
//        mergeGroupNumber++
//        state.isMultiSelectionModeEnabled = !state.isMultiSelectionModeEnabled
//    }
//    val onRemoveClicked: () -> Unit = {
//        selectedItemsInBox.forEach { item ->
//            optionStateViewModel.removeFromMergeGroup(item.mergeGroup ?: -1, listOf(item))
//        }
//        selectedItemsInBox.clear()
//        state.isMultiSelectionModeEnabled = !state.isMultiSelectionModeEnabled
//    }
//    val onAddIconClicked: (Int) -> Unit = { groupId ->
////        if (selectedItems.isNotEmpty()) {
////            selectedItems.forEach { item ->
////                mergedItemsContainers[groupId]!!.add(item)
////            }
////        }
//    }
//
//    if (signal == 1) {
//        OptionsInput(
//            onIncreaseButtonClicked = onIncreaseButtonClicked,
//            onDecreaseButtonClicked = onDecreaseButtonClicked,
//            onTickClicked = onTickClicked,
//            optionInputs = optionInputs.value
//        )
//    }
//    if (signal == 2) {
//        OptionsInMultiSelectionList(
//            optionList = optionList,
//            state = state,
//            mergeGroupNumber = mergeGroupNumber,
//            signal = signalForPopUp,
//            onMergeClicked = onMergeClicked,
//            onRemoveClicked = onRemoveClicked,
//            selectedItems = selectedItems,
//            selectedItemsInBox = selectedItemsInBox,
//            mergedItemsContainers = mergedItemsContainers,
//            filteredItems = filteredItems,
//            onSelectedItems = onSelectedItems,
//            onSelectedItemsInBox = onSelectedItemsInBox,
//            onAddClicked = onAddClicked,
//            onRightClicked = onRightClicked,
//            signalForAddIcon = signalForAddIcon,
//            onAddIconClicked = onAddIconClicked
//        )
//    }
//    if (signal == 3) {
//        OptionsPreviewSection(
//            options = optionList,
//            onAddClicked = onAddClicked,
//            onEditClicked = onEditClicked
//        )
//    }
//}
//
//@Composable
//private fun OptionsInput(
//    modifier: Modifier = Modifier,
//    onIncreaseButtonClicked: () -> Unit,
//    onDecreaseButtonClicked: () -> Unit,
//    onTickClicked: () -> Unit,
//    optionInputs: List<OptionStateInput>
//) {
//    val scrollState = rememberScrollState()
//    Box(
//        modifier.fillMaxSize(),
//        contentAlignment = Alignment.BottomEnd
//    ) {
//        Column(
//            modifier
//                .fillMaxSize()
//                .background(color = MaterialTheme.colorScheme.background)
//                .verticalScroll(scrollState),
//            verticalArrangement = Arrangement.spacedBy(8.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            optionInputs.forEach { input ->
//                OptionColumn(
//                    input = input
//                )
//            }
//            ButtonRow(
//                onIncreaseButtonClicked = onIncreaseButtonClicked,
//                onDecreaseButtonClicked = onDecreaseButtonClicked
//            )
//            Spacer(modifier = modifier.height(50.dp))
//        }
//        BoxForCheckButton(onTickClicked = onTickClicked)
//    }
//}
//
//class OptionStateViewModel : ViewModel() {
//    private val _optionInputs = MutableStateFlow<List<OptionStateInput>>(emptyList())
//    val optionInputs: StateFlow<List<OptionStateInput>> = _optionInputs
//
//    private val _mergedItemsContainers =
//        MutableStateFlow<MutableMap<Int, MutableList<OptionState>>>(mutableMapOf())
//    val mergedItemsContainers: StateFlow<Map<Int, List<OptionState>>> = _mergedItemsContainers
//
//    private val _filteredItems = MutableStateFlow<List<OptionState>>(emptyList())
//    val filteredItems: StateFlow<List<OptionState>> = _filteredItems
//
//    val optionStates: StateFlow<List<OptionState>> = _optionInputs.map { inputs ->
//        inputs.mapIndexed { index, input ->
//            OptionState(
//                id = index,
//                name = input.name.value,
//                price = input.price.value,
//                amount = input.amount.value,
//                upperLimit = input.upperLimit.value,
//                lowerLimit = input.lowerLimit.value,
//                mergeGroup = input.mergeGroup.value,
//                mergeId = input.mergeId.value
//            )
//        }
//    }.stateIn(
//        viewModelScope,
//        SharingStarted.Lazily,
//        emptyList()
//    )
//
//    init {
//        addNewOptionInput()
//
//        viewModelScope.launch {
//            _mergedItemsContainers.combine(optionStates) { mergedContainers, optionList ->
//                optionList.filter {
//                    it !in mergedContainers.values.flatten()
//                }
//            }.collect { filtered ->
//                _filteredItems.value = filtered
//            }
//        }
//    }
//
//    fun addNewOptionInput() {
//        val newInput =
//            OptionStateInput(
//                name = mutableStateOf(""),
//                price = mutableIntStateOf(0),
//                amount = mutableStateOf(null),
//                upperLimit = mutableStateOf(null),
//                lowerLimit = mutableStateOf(null),
//                mergeGroup = mutableStateOf(null),
//                mergeId = mutableStateOf(null)
//            )
//        _optionInputs.value = _optionInputs.value + newInput
//    }
//
//    fun removeLastOptionInput() {
//        if (_optionInputs.value.isNotEmpty()) {
//            _optionInputs.value = _optionInputs.value.dropLast(1)
//        }
//    }
//
//    fun addToMergeGroup(
//        groupId: Int,
//        items: List<OptionState>
//    ) {
//        _mergedItemsContainers.value[groupId] =
//            (_mergedItemsContainers.value[groupId] ?: mutableListOf()).apply {
//                addAll(items)
//            }
//        items.forEachIndexed { index, item ->
//            optionInputs.value[item.id].mergeGroup.value = groupId
//            optionInputs.value[item.id].mergeId.value = index
//        }
//    }
//
//    fun removeFromMergeGroup(
//        groupId: Int,
//        items: List<OptionState>
//    ) {
//        _mergedItemsContainers.value[groupId]?.removeAll(items)
//        items.forEach { item ->
//            optionInputs.value[item.id].mergeGroup.value = null
//            optionInputs.value[item.id].mergeId.value = null
//        }
//    }
//
//    data class OptionStateInput(
//        val name: MutableState<String>,
//        val price: MutableState<Int>,
//        val amount: MutableState<Int?>,
//        val upperLimit: MutableState<Int?>,
//        val lowerLimit: MutableState<Int?>,
//        val mergeGroup: MutableState<Int?>,
//        val mergeId: MutableState<Int?>
//    )
//}

//class OptionStateViewModel : ViewModel() {
//    val optionInputs = mutableStateListOf<OptionStateInput>(
//        OptionStateInput(
//            name = mutableStateOf("A"),
//            price = mutableStateOf(1),
//            amount = mutableStateOf(null),
//            upperLimit = mutableStateOf(null),
//            lowerLimit = mutableStateOf(null),
//            mergeGroup = mutableStateOf(null),
//            mergeId = mutableStateOf(null)
//        ),
//        OptionStateInput(
//            name = mutableStateOf("B"),
//            price = mutableStateOf(2),
//            amount = mutableStateOf(null),
//            upperLimit = mutableStateOf(null),
//            lowerLimit = mutableStateOf(null),
//            mergeGroup = mutableStateOf(null),
//            mergeId = mutableStateOf(null)
//        ),
//        OptionStateInput(
//            name = mutableStateOf("C"),
//            price = mutableStateOf(3),
//            amount = mutableStateOf(null),
//            upperLimit = mutableStateOf(null),
//            lowerLimit = mutableStateOf(null),
//            mergeGroup = mutableStateOf(null),
//            mergeId = mutableStateOf(null)
//        ),
//        OptionStateInput(
//            name = mutableStateOf("D"),
//            price = mutableStateOf(4),
//            amount = mutableStateOf(null),
//            upperLimit = mutableStateOf(null),
//            lowerLimit = mutableStateOf(null),
//            mergeGroup = mutableStateOf(null),
//            mergeId = mutableStateOf(null)
//        )
//    )
//
//    private val _mergedItemsContainers =
//        MutableStateFlow<MutableMap<Int, MutableList<OptionState>>>(mutableMapOf())
//    val mergedItemsContainers: StateFlow<Map<Int, List<OptionState>>> = _mergedItemsContainers
//
//    private val _filteredItems = MutableStateFlow<List<OptionState>>(emptyList())
//    val filteredItems: StateFlow<List<OptionState>> = _filteredItems
//
//    init {
////        addNewOptionInput()
//
//        viewModelScope.launch {
//            _mergedItemsContainers.combine(getOptionStatesFlow()) { mergedContainers, optionList ->
//                optionList.filter {
//                    it !in mergedContainers.values.flatten()
//                }
//            }
//                .collect { filtered ->
//                    _filteredItems.value = filtered
//                }
//        }
//    }
//
//    fun addNewOptionInput() {
//        optionInputs.add(
//            OptionStateInput(
//                name = mutableStateOf(""),
//                price = mutableIntStateOf(0),
//                amount = mutableStateOf(null),
//                upperLimit = mutableStateOf(null),
//                lowerLimit = mutableStateOf(null),
//                mergeGroup = mutableStateOf(null),
//                mergeId = mutableStateOf(null)
//            )
//        )
//    }
//
//    fun getOptionStates(): List<OptionState> {
//        return optionInputs.mapIndexed { index, input ->
//            OptionState(
//                id = index,
//                name = input.name.value,
//                price = input.price.value,
//                amount = input.amount.value,
//                upperLimit = input.upperLimit.value,
//                lowerLimit = input.lowerLimit.value,
//                mergeGroup = input.mergeGroup.value,
//                mergeId = input.mergeId.value
//            )
//        }
//    }
//
//    private fun getOptionStatesFlow(): Flow<List<OptionState>> = flow {
//        emit(getOptionStates())
//    }
//
//    fun addToMergeGroup(groupId: Int, items: List<OptionState>) {
//        _mergedItemsContainers.value[groupId] =
//            (_mergedItemsContainers.value[groupId] ?: mutableListOf()).apply {
//                addAll(items)
//            }
//        items.forEachIndexed { index, item ->
//            optionInputs[item.id].mergeGroup.value = groupId
//            optionInputs[item.id].mergeId.value = index
//        }
//    }
//
//    fun removeFromMergeGroup(groupId: Int, items: List<OptionState>) {
//        _mergedItemsContainers.value[groupId]?.removeAll(items)
//        items.forEach { item ->
//            optionInputs[item.id].mergeGroup.value = null
//            optionInputs[item.id].mergeId.value = null
//        }
//    }
//
//    data class OptionStateInput(
//        val name: MutableState<String>,
//        val price: MutableState<Int>,
//        val amount: MutableState<Int?>,
//        val upperLimit: MutableState<Int?>,
//        val lowerLimit: MutableState<Int?>,
//        val mergeGroup: MutableState<Int?>,
//        val mergeId: MutableState<Int?>
//    )
//}

@Composable
fun CarNameDropdown(onSelectionChanged: (String, String) -> Unit) {

    var brandDropDownControl by remember { mutableStateOf(false) }
    var modelDropDownControl by remember { mutableStateOf(false) }
    var selectedBrandIndex by remember { mutableIntStateOf(0) }
    var selectedModelIndex by remember { mutableIntStateOf(0) }

    val carBrands = listOf(
        "Brand",
        "Audi",
        "BMW",
        "Citroen",
        "Dacia",
        "Fiat",
        "Ford",
        "Honda",
        "Hyundai",
        "Kia",
        "Mercedes",
        "Nissan",
        "Peugeot",
        "Renault",
        "Seat",
        "Skoda",
        "Toyota",
        "Volkswagen",
        "Volvo"
    )
    val carModels = listOf(
        listOf("Model"),
        listOf("A1", "A3", "A4", "A5", "A6", "Q2", "Q3"),
        listOf("1 Series", "2 Series", "3 Series", "4 Series", "5 Series", "X1", "X3", "X5"),
        listOf(
            "C3",
            "C3 Aircross",
            "C4",
            "C4 X",
            "C5 Aircross",
            "C-Elysee",
            "Berlingo",
            "C5",
            "C1",
            "C3 Picasso",
            "C4 Cactus",
            "C4 Picasso",
            "Xsara",
            "Nemo"
        ),
        listOf("Duster", "Sandero", "Jogger", "Dokker", "Lodgy", "Logan MCV", "Logan"),
        listOf(
            "Egea",
            "Panda",
            "500X",
            "Fiorino",
            "Doblo Combi",
            "Doblo Cargo",
            "Linea",
            "500L",
            "Albea",
            "Brava",
            "Bravo",
            "Doblo",
            "Freemont",
            "Marea",
            "Palio",
            "Punto",
            "Siena",
            "Stilo",
            "Tempra",
            "Tipo",
            "Uno"
        ),
        listOf(
            "Fiesta",
            "Focus",
            "Puma",
            "Kuga",
            "Transit Courier",
            "Tourneo Courier",
            "Tourneo Connect",
            "Ranger",
            "B-Max",
            "C-Max",
            "EcoSport",
            "Escort",
            "Fusion",
            "Ka",
            "Mondeo",
            "Taunus",
            "Transit Connect"
        ),
        listOf("Civic", "City", "Accord", "Jazz", "HR-V", "CR-V", "CR-Z"),
        listOf(
            "i10",
            "i20",
            "Bayon",
            "Kona",
            "Elantra",
            "Tucson",
            "Accent",
            "Accent Blue",
            "Accent Era",
            "Excel",
            "Getz",
            "i30",
            "ix35"
        ),
        listOf(
            "Picanto",
            "Rio",
            "Stonic",
            "Cerato",
            "Ceed",
            "XCeed",
            "Sportage",
            "Sorento",
            "Pride",
            "Venga"
        ),
        listOf(
            "A Serisi",
            "B Serisi",
            "C Serisi",
            "CLA Serisi",
            "CLS",
            "E Serisi",
            "GLA Serisi",
            "Vito"
        ),
        listOf(
            "Juke",
            "Qashqai",
            "X-Trail",
            "Micra",
            "Navara",
            "Note",
            "Primera",
            "Pulsar",
            "Skystar"
        ),
        listOf(
            "208",
            "308",
            "2008",
            "408",
            "3008",
            "508",
            "5008",
            "Rifter",
            "Partner",
            "Bipper",
            "106",
            "107",
            "206",
            "207",
            "301",
            "306",
            "307",
            "407"
        ),
        listOf(
            "Clio",
            "Taliant",
            "Captur",
            "Megane",
            "Austral",
            "Koleos",
            "Kangoo",
            "Kadjar",
            "Fluence",
            "Laguna",
            "Latitude",
            "Symbol",
            "Talisman",
            "Scenic",
            "R 11",
            "R 19",
            "R 21",
            "R 9"
        ),
        listOf("Ibiza", "Leon", "Arona", "Ateca", "Altea", "Cordoba", "Toledo"),
        listOf(
            "Fabia",
            "Scala",
            "Octavia",
            "Superb",
            "Kamiq",
            "Karoq",
            "Kodiaq",
            "Citigo",
            "Rapid",
            "Roomster",
            "Yeti"
        ),
        listOf(
            "Corolla",
            "Yaris",
            "C-HR",
            "RAV4",
            "Proace City",
            "Hilux",
            "Auris",
            "Avensis",
            "Verso"
        ),
        listOf(
            "Polo",
            "Golf",
            "T-Cross",
            "Taigo",
            "T-Roc",
            "Tiguan",
            "Caddy",
            "Amarok",
            "Transporter",
            "Passat",
            "Jetta",
            "Scirocco",
            "CC",
            "Bora"
        ),
        listOf("V40", "XC40", "XC60", "XC90", "S40", "S60", "S80", "S90")
    )

    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {

        // Brand Dropdown
        OutlinedCard(modifier = Modifier.padding(16.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .width(125.dp)
                    .height(50.dp)
                    .padding(5.dp)
                    .clickable {
                        brandDropDownControl = true
                    }
            ) {
                Text(text = carBrands[selectedBrandIndex])
                Image(
                    painter = painterResource(id = R.drawable.baseline_keyboard_arrow_down_24),
                    contentDescription = ""
                )
            }

            DropdownMenu(
                expanded = brandDropDownControl,
                onDismissRequest = { brandDropDownControl = false },
                modifier = Modifier.requiredSizeIn(maxHeight = 300.dp)
            ) {
                carBrands.forEachIndexed { index, brand ->
                    DropdownMenuItem(
                        text = { Text(text = brand) },
                        onClick = {
                            brandDropDownControl = false
                            selectedBrandIndex = index
                            // Reset model index when brand changes
                            selectedModelIndex = 0
                            onSelectionChanged(
                                carBrands[selectedBrandIndex],
                                carModels[selectedBrandIndex][selectedModelIndex]
                            )
                        }
                    )
                }
            }
        }

        // Model Dropdown
        OutlinedCard(modifier = Modifier.padding(16.dp)) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .width(125.dp)
                    .height(50.dp)
                    .padding(5.dp)
                    .clickable {
                        modelDropDownControl = true
                    }
            ) {
                Text(text = carModels[selectedBrandIndex][selectedModelIndex])
                Image(
                    painter = painterResource(id = R.drawable.baseline_keyboard_arrow_down_24),
                    contentDescription = ""
                )
            }

            DropdownMenu(
                expanded = modelDropDownControl,
                onDismissRequest = { modelDropDownControl = false },
                modifier = Modifier.requiredSizeIn(maxHeight = 300.dp)
            ) {
                carModels[selectedBrandIndex].forEachIndexed { index, model ->
                    DropdownMenuItem(
                        text = { Text(text = model) },
                        onClick = {
                            modelDropDownControl = false
                            selectedModelIndex = index
                            onSelectionChanged(
                                carBrands[selectedBrandIndex],
                                carModels[selectedBrandIndex][selectedModelIndex]
                            )
                        }
                    )
                }
            }
        }
    }
}

//Example Implementation
//@OptIn(ExperimentalMaterial3Api::class)
//@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
//@Composable
//fun MultiDropDownMenu() {
//    var selectedBrand by remember { mutableStateOf("") }
//    var selectedModel by remember { mutableStateOf("") }
//    val tfBrandModel = remember { mutableStateOf("") }
//    val scrollState = rememberScrollState()
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = {
//                    Text(text = "Multi DropdownMenu")
//                },
//            )
//        },
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .verticalScroll(scrollState),
//            verticalArrangement = Arrangement.Top,
//            horizontalAlignment = Alignment.CenterHorizontally,
//        ) {
//            Spacer(modifier = Modifier.size(100.dp))
//            CarNameDropdown { brand, model ->
//                selectedBrand = brand
//                selectedModel = model
//            }
//            Spacer(modifier = Modifier.size(20.dp))
//            Button(
//                onClick = {tfBrandModel.value = "$selectedBrand - $selectedModel"},
//                modifier = Modifier.size(250.dp, 50.dp)
//            ) {
//                Text(text = "Register")
//            }
//            Spacer(modifier = Modifier.size(50.dp))
//            OutlinedTextField(
//                value = tfBrandModel.value,
//                onValueChange = {tfBrandModel.value = it},
//                label = { Text(text = "Brand - Model")}
//            )
//        }
//    }
//}