package com.kampplus.hava.ui.city

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kampplus.hava.R
import com.kampplus.hava.data.ApiClient
import com.kampplus.hava.model.AqiData
import com.kampplus.hava.model.City
import com.kampplus.hava.ui.AppLanguage
import com.kampplus.hava.ui.AppStrings
import com.kampplus.hava.ui.LoadState
import com.kampplus.hava.ui.SettingsBottomSheet
import com.kampplus.hava.ui.SettingsManager
import com.kampplus.hava.ui.component.AirQualityDetailSheet
import com.kampplus.hava.ui.component.EmptyView
import com.kampplus.hava.ui.component.ErrorView
import com.kampplus.hava.ui.component.ShimmerList
import com.kampplus.hava.ui.theme.HavaTheme
import kotlinx.coroutines.delay

val DEFAULT_QUICK_ACCESS_CITIES = listOf(
    City(
        id = 311004,
        name = "Hatay",
        latitude = 36.2023,
        longitude = 36.1606,
        countryCode = "TR",
        country = "Türkiye",
        admin1 = "Hatay"
    ),
    City(
        id = 745044,
        name = "İstanbul",
        latitude = 41.0138,
        longitude = 28.9603,
        countryCode = "TR",
        country = "Türkiye",
        admin1 = "İstanbul"
    ),
    City(
        id = 323786,
        name = "Ankara",
        latitude = 39.9199,
        longitude = 32.8543,
        countryCode = "TR",
        country = "Türkiye",
        admin1 = "Ankara"
    )
)

@Composable
fun CityRoute(onCityClick: (City) -> Unit, modifier: Modifier = Modifier, viewModel: CityViewModel = viewModel()) {
    val query by viewModel.query.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val aqiData by viewModel.aqiState.collectAsStateWithLifecycle()

    CityScreen(
        query = query,
        uiState = uiState,
        aqiData = aqiData,
        onQueryChange = { newQuery ->
            viewModel.onQueryChange(newQuery)
            if (newQuery.trim().length >= 2) {
                viewModel.search()
            }
        },
        onSearch = viewModel::search,
        onSelectAirQualityCity = { selectedCityName ->
            viewModel.fetchAirQuality(selectedCityName)
        },
        onCityClick = onCityClick,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityScreen(
    query: String,
    uiState: LoadState<List<City>>,
    aqiData: AqiData?,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onSelectAirQualityCity: (String) -> Unit,
    onCityClick: (City) -> Unit,
    modifier: Modifier = Modifier
) {
    var showPlanSheet by remember { mutableStateOf(false) }
    var showSettingsSheet by remember { mutableStateOf(false) }
    var showAddCityDialog by remember { mutableStateOf(false) }

    var aqiQuery by remember { mutableStateOf("") }
    var aqiSearchResults by remember { mutableStateOf<List<City>>(emptyList()) }
    var isAqiSearching by remember { mutableStateOf(false) }
    var selectedAqiCityName by remember { mutableStateOf<String?>(null) }
    var showAqiDetailSheet by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(aqiQuery) {
        val trimmed = aqiQuery.trim()
        if (trimmed.length >= 2) {
            isAqiSearching = true
            delay(300)
            try {
                val response = ApiClient.cityApi.search(name = trimmed)
                aqiSearchResults = response.results ?: emptyList()
            } catch (e: Exception) {
                aqiSearchResults = emptyList()
            } finally {
                isAqiSearching = false
            }
        } else {
            aqiSearchResults = emptyList()
            isAqiSearching = false
        }
    }

    val quickAccessCities = remember {
        mutableStateListOf<City>().apply {
            addAll(DEFAULT_QUICK_ACCESS_CITIES)
        }
    }

    val settings by SettingsManager.settings.collectAsStateWithLifecycle()
    val strings = AppStrings.get(settings.language)
    val isEn = settings.language == AppLanguage.ENGLISH

    val isDark = isSystemInDarkTheme()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (query.trim().isBlank()) {
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(id = R.drawable.arkaplan),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(if (isDark) 0.14f else 0.22f),
                    contentScale = ContentScale.Crop
                )

                if (isDark) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.85f))
                    )
                }
            }
        }

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text(strings.appName) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    ),
                    actions = {
                        IconButton(onClick = { showSettingsSheet = true }) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = strings.settingsTitle
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                SearchBar(
                    query = query,
                    hint = strings.searchHint,
                    onQueryChange = onQueryChange,
                    onSearch = onSearch,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = if (query.trim().isBlank()) Alignment.TopStart else Alignment.Center
                ) {
                    if (query.trim().isBlank()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState())
                                .padding(horizontal = 20.dp, vertical = 12.dp),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = strings.welcomeTitle,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = strings.welcomeSubtitle,
                                style = MaterialTheme.typography.bodyMedium,
                                fontStyle = FontStyle.Italic,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            Text(
                                text = if (isEn) "Quick Access" else "Hızlı Erişim",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            LazyRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                items(items = quickAccessCities, key = { it.id }) { city ->
                                    Card(
                                        onClick = { onCityClick(city) },
                                        modifier = Modifier
                                            .width(108.dp)
                                            .border(
                                                width = 1.dp,
                                                color = if (isDark) {
                                                    Color.White.copy(alpha = 0.15f)
                                                } else {
                                                    Color.White.copy(alpha = 0.35f)
                                                },
                                                shape = RoundedCornerShape(16.dp)
                                            ),
                                        shape = RoundedCornerShape(16.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = if (isDark) {
                                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.40f)
                                            } else {
                                                Color.White.copy(alpha = 0.12f)
                                            }
                                        )
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 14.dp, horizontal = 8.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(text = "📍", fontSize = 20.sp)
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = city.name,
                                                style = MaterialTheme.typography.labelLarge,
                                                fontWeight = FontWeight.SemiBold,
                                                color = MaterialTheme.colorScheme.onSurface,
                                                maxLines = 1
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedButton(
                                onClick = { showAddCityDialog = true },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(46.dp),
                                shape = RoundedCornerShape(14.dp),
                                border = BorderStroke(
                                    1.dp,
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = if (isEn) "Add City" else "Şehir Ekle",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (isEn) "Add Quick Access City" else "Hızlı Erişim Şehri Ekle",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                            Spacer(modifier = Modifier.height(26.dp))

                            Text(
                                text = strings.aqiSectionTitle,
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = aqiQuery,
                                onValueChange = { aqiQuery = it },
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = { Text(strings.aqiSearchPlaceholder) },
                                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                                trailingIcon = {
                                    if (aqiQuery.isNotEmpty()) {
                                        IconButton(onClick = {
                                            aqiQuery = ""
                                            aqiSearchResults = emptyList()
                                        }) {
                                            Icon(Icons.Default.Clear, contentDescription = null)
                                        }
                                    }
                                },
                                singleLine = true,
                                shape = RoundedCornerShape(14.dp)
                            )

                            if (isAqiSearching) {
                                Spacer(modifier = Modifier.height(6.dp))
                                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                            }

                            if (aqiSearchResults.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Card(
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(6.dp)) {
                                        aqiSearchResults.take(5).forEach { city ->
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clickable {
                                                        keyboardController?.hide()
                                                        selectedAqiCityName = city.name
                                                        aqiSearchResults = emptyList()
                                                        aqiQuery = ""
                                                        onSelectAirQualityCity(city.name)
                                                        showAqiDetailSheet = true
                                                    }
                                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(text = "🍃", fontSize = 16.sp)
                                                Spacer(modifier = Modifier.width(10.dp))
                                                Column {
                                                    Text(
                                                        text = city.name,
                                                        style = MaterialTheme.typography.bodyMedium,
                                                        fontWeight = FontWeight.SemiBold
                                                    )
                                                    Text(
                                                        text = "${city.admin1 ?: ""}, ${city.country ?: ""}".trim().trim(','),
                                                        style = MaterialTheme.typography.bodySmall,
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            ElevatedButton(
                                onClick = { showPlanSheet = true },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Icon(imageVector = Icons.Default.DateRange, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(strings.planButton)
                            }
                        }
                    } else {
                        when (uiState) {
                            LoadState.Loading -> ShimmerList()
                            is LoadState.Success -> {
                                if (uiState.data.isEmpty()) {
                                    EmptyView(
                                        title = if (isEn) "City not found" else "Şehir bulunamadı",
                                        message = if (isEn) {
                                            "Check the spelling or try another city."
                                        } else {
                                            "Yazımı kontrol et ya da başka bir şehir dene."
                                        }
                                    )
                                } else {
                                    CityList(
                                        cities = uiState.data,
                                        onCityClick = onCityClick
                                    )
                                }
                            }
                            LoadState.Empty -> EmptyView(
                                title = if (isEn) "City not found" else "Şehir bulunamadı",
                                message = if (isEn) {
                                    "Check the spelling or try another city."
                                } else {
                                    "Yazımı kontrol et ya da başka bir şehir dene."
                                }
                            )
                            is LoadState.Error -> ErrorView(message = uiState.message, onRetry = onSearch)
                        }
                    }
                }
            }
        }
    }

    if (showAqiDetailSheet && selectedAqiCityName != null) {
        AirQualityDetailSheet(
            cityName = selectedAqiCityName!!,
            aqiData = aqiData,
            isLoading = aqiData == null,
            strings = strings,
            isEn = isEn,
            onDismiss = {
                showAqiDetailSheet = false
                selectedAqiCityName = null
            }
        )
    }

    if (showAddCityDialog) {
        AddQuickAccessCityDialog(
            isEn = isEn,
            onDismiss = { showAddCityDialog = false },
            onCitySelected = { newCity ->
                val alreadyExists = quickAccessCities.any {
                    it.id == newCity.id || it.name.trim().equals(newCity.name.trim(), ignoreCase = true)
                }
                if (!alreadyExists) {
                    quickAccessCities.add(newCity)
                }
            }
        )
    }

    if (showPlanSheet) {
        PlanBottomSheet(
            onDismiss = { showPlanSheet = false },
            onNavigateToCity = { selectedCity ->
                onCityClick(selectedCity)
            }
        )
    }

    if (showSettingsSheet) {
        SettingsBottomSheet(
            onDismiss = { showSettingsSheet = false }
        )
    }
}

@Composable
fun AddQuickAccessCityDialog(isEn: Boolean, onDismiss: () -> Unit, onCitySelected: (City) -> Unit) {
    var searchQuery by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf<List<City>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(searchQuery) {
        val trimmed = searchQuery.trim()
        if (trimmed.length >= 2) {
            isLoading = true
            delay(350)
            try {
                val response = ApiClient.cityApi.search(name = trimmed)
                searchResults = response.results ?: emptyList()
            } catch (e: Exception) {
                searchResults = emptyList()
            } finally {
                isLoading = false
            }
        } else {
            searchResults = emptyList()
            isLoading = false
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = if (isEn) "Add Quick Access City" else "Hızlı Erişim Şehri Ekle",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text(if (isEn) "Type city name..." else "Şehir adı yazın...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = null)
                            }
                        }
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(28.dp))
                    }
                } else if (searchQuery.trim().length >= 2 && searchResults.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isEn) "No matching city" else "Eşleşen şehir bulunamadı",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else if (searchResults.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(searchResults, key = { it.id }) { city ->
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        keyboardController?.hide()
                                        onCitySelected(city)
                                        onDismiss()
                                    }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = "📍", fontSize = 16.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = city.name,
                                            style = MaterialTheme.typography.bodyLarge,
                                            fontWeight = FontWeight.Medium
                                        )
                                        Text(
                                            text = "${city.admin1 ?: ""}, ${city.country ?: ""}".trim().trim(','),
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(if (isEn) "Cancel" else "Vazgeç")
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchBar(query: String, hint: String, onQueryChange: (String) -> Unit, onSearch: () -> Unit, modifier: Modifier = Modifier) {
    val keyboardController = LocalSoftwareKeyboardController.current
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier.fillMaxWidth(),
        label = { Text(hint) },
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(Icons.Filled.Clear, contentDescription = stringResource(R.string.action_clear))
                }
            }
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = {
                keyboardController?.hide()
                onSearch()
            }
        )
    )
}

@Composable
private fun CityList(cities: List<City>, onCityClick: (City) -> Unit, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items = cities, key = { it.id }) { city ->
            CityCard(city = city, onClick = { onCityClick(city) })
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CityScreenPreview() {
    HavaTheme {
        CityScreen(
            query = "Ankara",
            uiState = LoadState.Success(
                listOf(
                    City(
                        id = 323786,
                        name = "Ankara",
                        latitude = 39.91987,
                        longitude = 32.85427,
                        countryCode = "TR",
                        country = "Türkiye",
                        admin1 = "Ankara"
                    ),
                    City(
                        id = 738648,
                        name = "Ankara",
                        latitude = 41.0050,
                        longitude = 39.7269,
                        countryCode = "TR",
                        country = "Türkiye",
                        admin1 = "Trabzon"
                    )
                )
            ),
            aqiData = null,
            onQueryChange = {},
            onSearch = {},
            onSelectAirQualityCity = {},
            onCityClick = {}
        )
    }
}
