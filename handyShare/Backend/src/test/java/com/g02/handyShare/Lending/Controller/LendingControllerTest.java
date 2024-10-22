// package com.g02.handyShare.Lending.Controller;

// import com.g02.handyShare.Lending.Entity.LentItem;
// import com.g02.handyShare.Lending.Service.LentItemService;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;
// import org.springframework.http.ResponseEntity;

// import java.util.Arrays;
// import java.util.List;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.mockito.Mockito.*;

// public class LendingControllerTest {

//     @Mock
//     private LentItemService lentItemService;

//     @InjectMocks
//     private LendingController lendingController;

//     @BeforeEach
//     void setUp() {
//         MockitoAnnotations.openMocks(this);
//     }

//     @Test
//     void getAllLentItems() {
//         LentItem item1 = new LentItem();
//         item1.setName("Test Item 1");
//         LentItem item2 = new LentItem();
//         item2.setName("Test Item 2");
//         List<LentItem> lentItems = Arrays.asList(item1, item2);

//         when(lentItemService.getAllLentItems()).thenReturn(lentItems);

//         ResponseEntity<List<LentItem>> response = lendingController.getAllLentItems();

//         assertEquals(200, response.getStatusCodeValue());
//         assertEquals(2, response.getBody().size());
//         verify(lentItemService, times(1)).getAllLentItems();
//     }

//     @Test
//     void addLentItem() {
//         LentItem newItem = new LentItem();
//         newItem.setName("New Test Item");

//         when(lentItemService.addLentItem(any(LentItem.class))).thenReturn(newItem);

//         ResponseEntity<LentItem> response = lendingController.addLentItem(newItem);

//         assertEquals(201, response.getStatusCodeValue());
//         assertEquals("New Test Item", response.getBody().getName());
//         verify(lentItemService, times(1)).addLentItem(any(LentItem.class));
//     }
// }
