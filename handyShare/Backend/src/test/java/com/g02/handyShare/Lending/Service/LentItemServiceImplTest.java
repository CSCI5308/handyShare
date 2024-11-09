package com.g02.handyShare.Lending.Service;

import com.g02.handyShare.Lending.Entity.LentItem;
import com.g02.handyShare.Lending.Repository.LentItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LentItemServiceImplTest {

    @Mock
    private LentItemRepository lentItemRepository;

    @InjectMocks
    private LentItemServiceImpl lentItemService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllLentItems_ShouldReturnListOfLentItems() {
        List<LentItem> mockLentItems = new ArrayList<>();
        LentItem item = new LentItem();
        item.setId(1L);
        item.setName("Drill");
        item.setDescription("Powerful drill");
        item.setPrice(100.0);
        item.setAvailability("Available");
        item.setCategory("Tools");
        item.setCity("CityA");
        item.setState("StateA");
        item.setPincode("12345");
        item.setAddress("AddressA");
        item.setImageName("image.jpg");

        mockLentItems.add(item);
        when(lentItemRepository.findAll()).thenReturn(mockLentItems);

        List<LentItem> lentItems = lentItemService.getAllLentItems();

        assertEquals(1, lentItems.size());
        verify(lentItemRepository, times(1)).findAll();
    }

    @Test
    void addLentItem_ShouldReturnSavedLentItem() {
        LentItem lentItem = new LentItem();
        lentItem.setName("Saw");
        lentItem.setDescription("Electric saw");
        lentItem.setPrice(150.0);
        lentItem.setAvailability("Available");
        lentItem.setCategory("Tools");
        lentItem.setCity("CityB");
        lentItem.setState("StateB");
        lentItem.setPincode("67890");
        lentItem.setAddress("AddressB");
        lentItem.setImageName("saw.jpg");

        LentItem savedLentItem = new LentItem();
        savedLentItem.setId(1L);
        savedLentItem.setName("Saw");
        savedLentItem.setDescription("Electric saw");
        savedLentItem.setPrice(150.0);
        savedLentItem.setAvailability("Available");
        savedLentItem.setCategory("Tools");
        savedLentItem.setCity("CityB");
        savedLentItem.setState("StateB");
        savedLentItem.setPincode("67890");
        savedLentItem.setAddress("AddressB");
        savedLentItem.setImageName("saw.jpg");

        when(lentItemRepository.save(lentItem)).thenReturn(savedLentItem);

        LentItem result = lentItemService.addLentItem(lentItem);

        assertNotNull(result.getId());
        assertEquals("Saw", result.getName());
        verify(lentItemRepository, times(1)).save(lentItem);
    }

    @Test
    void updateLentItem_ShouldReturnUpdatedLentItem() {
        Long id = 1L;
        LentItem existingLentItem = new LentItem();
        existingLentItem.setId(id);
        existingLentItem.setName("Old Drill");
        existingLentItem.setDescription("Old drill description");
        existingLentItem.setPrice(80.0);
        existingLentItem.setAvailability("Available");
        existingLentItem.setCategory("Tools");
        existingLentItem.setCity("CityC");
        existingLentItem.setState("StateC");
        existingLentItem.setPincode("55555");
        existingLentItem.setAddress("Old Address");
        existingLentItem.setImageName("old_image.jpg");

        LentItem updatedLentItemDetails = new LentItem();
        updatedLentItemDetails.setName("Updated Drill");
        updatedLentItemDetails.setDescription("Updated drill description");
        updatedLentItemDetails.setPrice(120.0);
        updatedLentItemDetails.setAvailability("Not Available");
        updatedLentItemDetails.setCategory("Tools");
        updatedLentItemDetails.setCity("CityD");
        updatedLentItemDetails.setState("StateD");
        updatedLentItemDetails.setPincode("77777");
        updatedLentItemDetails.setAddress("New Address");
        updatedLentItemDetails.setImageName("new_image.jpg");

        when(lentItemRepository.findById(id)).thenReturn(Optional.of(existingLentItem));
        when(lentItemRepository.save(any(LentItem.class))).thenReturn(existingLentItem);

        LentItem result = lentItemService.updateLentItem(id, updatedLentItemDetails);

        assertEquals("Updated Drill", result.getName());
        assertEquals(120.0, result.getPrice());
        assertEquals("CityD", result.getCity());
        verify(lentItemRepository, times(1)).findById(id);
        verify(lentItemRepository, times(1)).save(existingLentItem);
    }

    @Test
    void updateLentItem_WhenItemNotFound_ShouldThrowException() {
        Long id = 2L;
        LentItem updatedLentItemDetails = new LentItem();
        updatedLentItemDetails.setName("Updated Drill");

        when(lentItemRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> lentItemService.updateLentItem(id, updatedLentItemDetails));

        assertEquals("Item not found", exception.getMessage());
        verify(lentItemRepository, times(1)).findById(id);
        verify(lentItemRepository, never()).save(any(LentItem.class));
    }

    @Test
    void deleteLentItem_ShouldCallDeleteById() {
        Long id = 1L;
        when(lentItemRepository.existsById(id)).thenReturn(true);

        lentItemService.deleteLentItem(id);

        verify(lentItemRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteLentItem_WhenItemNotFound_ShouldThrowException() {
        Long id = 2L;
        when(lentItemRepository.existsById(id)).thenReturn(false);

        Exception exception = assertThrows(RuntimeException.class, () -> lentItemService.deleteLentItem(id));

        assertEquals("Item not found", exception.getMessage());
        verify(lentItemRepository, times(1)).existsById(id);
        verify(lentItemRepository, never()).deleteById(anyLong());
    }
}
