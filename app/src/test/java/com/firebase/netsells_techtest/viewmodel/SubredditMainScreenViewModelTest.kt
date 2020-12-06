package com.firebase.netsells_techtest.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.firebase.netsells_techtest.R
import com.firebase.netsells_techtest.data.HotSubmissionsService
import com.firebase.netsells_techtest.model.HotSubData
import com.firebase.netsells_techtest.model.RedditApiResponseChildren
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.powermock.core.classloader.annotations.PrepareForTest

class SubredditMainScreenViewModelTest {

    private val LIST_INCREMENT = 8

    private lateinit var viewModel: SubredditMainScreenViewModel

    @Mock
    private lateinit var mockService: HotSubmissionsService

    @get:Rule
    var rule = InstantTaskExecutorRule()


    @Before
    fun setUp() {
        mockService = mock(HotSubmissionsService::class.java)
        viewModel = SubredditMainScreenViewModel(mockService)
    }

    //<editor-fold desc="formatText method tests">
    //test the logic for the list refactoring method
    @Test
    fun listOf3RedditApiResponseChildrenReturnlistOf3HotSubs() {

        val hotSubDataOne = HotSubData(title = "title one", author = "author one", url = "url one")
        val hotSubDataTwo = HotSubData(title = "title two", author = "author two", url = "url two")
        val hotSubDataThree =
            HotSubData(title = "title three", author = "author three", url = "url three")

        val redChildOne = RedditApiResponseChildren(data = hotSubDataOne, kind = "")
        val redChildTwo = RedditApiResponseChildren(data = hotSubDataTwo, kind = "")
        val redChildThree = RedditApiResponseChildren(data = hotSubDataThree, kind = "")

        val inputListOf3RedditApiResponseChildren =
            listOf<RedditApiResponseChildren>(redChildOne, redChildTwo, redChildThree)


        val actualProcessedList = viewModel.formatText(inputListOf3RedditApiResponseChildren)

        assertTrue(actualProcessedList is List<HotSubData>)
        assertTrue(actualProcessedList?.get(0)?.title == inputListOf3RedditApiResponseChildren.get(0).data?.title)

        assertEquals(inputListOf3RedditApiResponseChildren.size, actualProcessedList?.size)
    }

    @Test
    fun listReturnsAuthorWithPrefixText() {

        val hotSubDataOne = HotSubData(title = "title one", author = "author one", url = "url one")
        val hotSubDataTwo = HotSubData(title = "title two", author = "author two", url = "url two")
        val hotSubDataThree =
            HotSubData(title = "title three", author = "author three", url = "url three")

        val redChildOne = RedditApiResponseChildren(data = hotSubDataOne, kind = "")
        val redChildTwo = RedditApiResponseChildren(data = hotSubDataTwo, kind = "")
        val redChildThree = RedditApiResponseChildren(data = hotSubDataThree, kind = "")

        val inputListOf3RedditApiResponseChildren =
            listOf<RedditApiResponseChildren>(redChildOne, redChildTwo, redChildThree)

        val actualProcessedList = viewModel.formatText(inputListOf3RedditApiResponseChildren)

        assertTrue(actualProcessedList?.get(0)?.title == hotSubDataOne.title)
        assertTrue(actualProcessedList?.get(0)?.author != hotSubDataOne.author)
        assertTrue(actualProcessedList?.get(0)?.author == "Post by ${hotSubDataOne.author}")

    }

    @Test
    fun listReturnsTwoHotSubDataWhenThreePassedWithOneNull() {

        val hotSubDataOne = HotSubData(title = "title one", author = "author one", url = "url one")
        val hotSubDataTwo = HotSubData(title = "title two", author = "author two", url = "url two")

        val redChildOne = RedditApiResponseChildren(data = hotSubDataOne, kind = "")
        val redChildTwo = RedditApiResponseChildren(data = hotSubDataTwo, kind = "")
        val redChildThree = null

        val inputListOf3RedditApiResponseChildren = listOf(redChildOne, redChildTwo, redChildThree)

        val actualProcessedList = viewModel.formatText(inputListOf3RedditApiResponseChildren)

        assertEquals(actualProcessedList?.size, 2)
        assertNotNull(actualProcessedList?.get(0))
        assertNotNull(actualProcessedList?.get(1))

    }
    //</editor-fold>

    //<editor-fold desc="ui list update tests">
    //test add more items functionality
    @Test
    fun listOnlyUpdatesAfewItemsToUI() {
        //hpw many items are in the live data list
        val initialListSize = viewModel.apiDataList.value?.size ?: 0
        val expectedListSize = initialListSize!! + LIST_INCREMENT

        //populate the list items list with fake items
        var mockItemsList = arrayListOf<HotSubData>()
        for (i in 0..25) {
            mockItemsList.add(HotSubData("", "", ""))
        }
        viewModel.allApiItemsList = mockItemsList
        viewModel.numberOfItemsToDisplay = LIST_INCREMENT
        //do add more items method
        viewModel.addMoreListItems()

        val uiListItemsSize = viewModel.apiDataList.value?.size
        assertEquals(expectedListSize, uiListItemsSize)

    }

    @Test
    fun listOnlyAddsElementsUpToMaxSizeOfList() {
        //hpw many items are in the live data list
        val expectedListSize = 5

        //populate the list items list with fake items
        var mockItemsList = arrayListOf<HotSubData>()
        for (i in 0..4) {
            mockItemsList.add(HotSubData("", "", ""))
        }
        viewModel.allApiItemsList = mockItemsList
        viewModel.numberOfItemsToDisplay = LIST_INCREMENT
        //do add more items method
        viewModel.addMoreListItems()

        val uiListItemsSize = viewModel.apiDataList.value?.size
        assertEquals(uiListItemsSize, 5)

    }
    //</editor-fold>



}