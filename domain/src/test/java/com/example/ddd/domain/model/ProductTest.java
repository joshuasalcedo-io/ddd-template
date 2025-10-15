package com.example.ddd.domain.model;

import com.example.ddd.domain.aggregateroot.product.Product;
import com.example.ddd.domain.aggregateroot.product.ProductId;
import com.example.ddd.domain.aggregateroot.product.ProductStatus;
import com.example.ddd.domain.event.ProductCreatedEvent;
import com.example.ddd.domain.event.ProductPriceChangedEvent;
import com.example.ddd.domain.exception.InvalidDomainStateException;
import org.jmolecules.event.types.DomainEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Product Aggregate Tests")
class ProductTest {

    private static final String VALID_NAME = "Test Product";
    private static final String VALID_DESCRIPTION = "Test Description";
    private static final Money VALID_PRICE = Money.of(BigDecimal.valueOf(100.00), Currency.getInstance("USD"));
    private static final int VALID_STOCK = 10;

    @Nested
    @DisplayName("Product Creation")
    class ProductCreation {

        @Test
        @DisplayName("should create product with valid data")
        void shouldCreateProductWithValidData() {
            // When
            Product product = Product.create(VALID_NAME, VALID_DESCRIPTION, VALID_PRICE, VALID_STOCK);

            // Then
            assertThat(product).isNotNull();
            assertThat(product.getId()).isNotNull();
            assertThat(product.getName()).isEqualTo(VALID_NAME);
            assertThat(product.getDescription()).isEqualTo(VALID_DESCRIPTION);
            assertThat(product.getPrice()).isEqualTo(VALID_PRICE);
            assertThat(product.getStockQuantity()).isEqualTo(VALID_STOCK);
            assertThat(product.getStatus()).isEqualTo(ProductStatus.ACTIVE);
            assertThat(product.getCreatedAt()).isNotNull();
            assertThat(product.getUpdatedAt()).isNotNull();
        }

        @Test
        @DisplayName("should register ProductCreatedEvent when creating product")
        void shouldRegisterProductCreatedEventWhenCreatingProduct() {
            // When
            Product product = Product.create(VALID_NAME, VALID_DESCRIPTION, VALID_PRICE, VALID_STOCK);

            // Then
            List<DomainEvent> events = product.getDomainEvents();
            assertThat(events).hasSize(1);
            assertThat(events.get(0)).isInstanceOf(ProductCreatedEvent.class);

            ProductCreatedEvent event = (ProductCreatedEvent) events.get(0);
            assertThat(event.productId()).isEqualTo(product.getId());
            assertThat(event.productName()).isEqualTo(VALID_NAME);
            assertThat(event.price()).isEqualTo(VALID_PRICE);
        }

        @Test
        @DisplayName("should throw exception when name is null")
        void shouldThrowExceptionWhenNameIsNull() {
            // When/Then
            assertThatThrownBy(() -> Product.create(null, VALID_DESCRIPTION, VALID_PRICE, VALID_STOCK))
                .isInstanceOf(InvalidDomainStateException.class)
                .hasMessageContaining("Product name cannot be null or blank");
        }

        @Test
        @DisplayName("should throw exception when name is blank")
        void shouldThrowExceptionWhenNameIsBlank() {
            // When/Then
            assertThatThrownBy(() -> Product.create("   ", VALID_DESCRIPTION, VALID_PRICE, VALID_STOCK))
                .isInstanceOf(InvalidDomainStateException.class)
                .hasMessageContaining("Product name cannot be null or blank");
        }

        @Test
        @DisplayName("should throw exception when name exceeds 255 characters")
        void shouldThrowExceptionWhenNameExceeds255Characters() {
            // Given
            String longName = "a".repeat(256);

            // When/Then
            assertThatThrownBy(() -> Product.create(longName, VALID_DESCRIPTION, VALID_PRICE, VALID_STOCK))
                .isInstanceOf(InvalidDomainStateException.class)
                .hasMessageContaining("Product name cannot exceed 255 characters");
        }

        @Test
        @DisplayName("should throw exception when price is null")
        void shouldThrowExceptionWhenPriceIsNull() {
            // When/Then
            assertThatThrownBy(() -> Product.create(VALID_NAME, VALID_DESCRIPTION, null, VALID_STOCK))
                .isInstanceOf(InvalidDomainStateException.class)
                .hasMessageContaining("Price cannot be null");
        }

        @Test
        @DisplayName("should throw exception when stock is negative")
        void shouldThrowExceptionWhenStockIsNegative() {
            // When/Then
            assertThatThrownBy(() -> Product.create(VALID_NAME, VALID_DESCRIPTION, VALID_PRICE, -1))
                .isInstanceOf(InvalidDomainStateException.class)
                .hasMessageContaining("Stock quantity cannot be negative");
        }

        @Test
        @DisplayName("should create product with zero stock")
        void shouldCreateProductWithZeroStock() {
            // When
            Product product = Product.create(VALID_NAME, VALID_DESCRIPTION, VALID_PRICE, 0);

            // Then
            assertThat(product.getStockQuantity()).isZero();
        }
    }

    @Nested
    @DisplayName("Price Management")
    class PriceManagement {

        @Test
        @DisplayName("should change price and register event")
        void shouldChangePriceAndRegisterEvent() {
            // Given
            Product product = Product.create(VALID_NAME, VALID_DESCRIPTION, VALID_PRICE, VALID_STOCK);
            product.clearDomainEvents();
            Money newPrice = Money.of(BigDecimal.valueOf(150.00), Currency.getInstance("USD"));

            // When
            product.changePrice(newPrice);

            // Then
            assertThat(product.getPrice()).isEqualTo(newPrice);

            List<DomainEvent> events = product.getDomainEvents();
            assertThat(events).hasSize(1);
            assertThat(events.get(0)).isInstanceOf(ProductPriceChangedEvent.class);

            ProductPriceChangedEvent event = (ProductPriceChangedEvent) events.get(0);
            assertThat(event.productId()).isEqualTo(product.getId());
            assertThat(event.oldPrice()).isEqualTo(VALID_PRICE);
            assertThat(event.newPrice()).isEqualTo(newPrice);
        }

        @Test
        @DisplayName("should throw exception when changing price to null")
        void shouldThrowExceptionWhenChangingPriceToNull() {
            // Given
            Product product = Product.create(VALID_NAME, VALID_DESCRIPTION, VALID_PRICE, VALID_STOCK);

            // When/Then
            assertThatThrownBy(() -> product.changePrice(null))
                .isInstanceOf(InvalidDomainStateException.class)
                .hasMessageContaining("Price cannot be null");
        }

        @Test
        @DisplayName("should update updatedAt timestamp when changing price")
        void shouldUpdateTimestampWhenChangingPrice() {
            // Given
            Product product = Product.create(VALID_NAME, VALID_DESCRIPTION, VALID_PRICE, VALID_STOCK);
            Instant originalUpdatedAt = product.getUpdatedAt();

            // When
            try {
                Thread.sleep(10); // Small delay to ensure timestamp changes
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            Money newPrice = Money.of(BigDecimal.valueOf(150.00), Currency.getInstance("USD"));
            product.changePrice(newPrice);

            // Then
            assertThat(product.getUpdatedAt()).isAfter(originalUpdatedAt);
        }
    }

    @Nested
    @DisplayName("Stock Management")
    class StockManagement {

        @Test
        @DisplayName("should add stock successfully")
        void shouldAddStockSuccessfully() {
            // Given
            Product product = Product.create(VALID_NAME, VALID_DESCRIPTION, VALID_PRICE, VALID_STOCK);

            // When
            product.addStock(5);

            // Then
            assertThat(product.getStockQuantity()).isEqualTo(15);
        }

        @Test
        @DisplayName("should throw exception when adding zero or negative stock")
        void shouldThrowExceptionWhenAddingZeroOrNegativeStock() {
            // Given
            Product product = Product.create(VALID_NAME, VALID_DESCRIPTION, VALID_PRICE, VALID_STOCK);

            // When/Then
            assertThatThrownBy(() -> product.addStock(0))
                .isInstanceOf(InvalidDomainStateException.class)
                .hasMessageContaining("Quantity to add must be positive");

            assertThatThrownBy(() -> product.addStock(-5))
                .isInstanceOf(InvalidDomainStateException.class)
                .hasMessageContaining("Quantity to add must be positive");
        }

        @Test
        @DisplayName("should remove stock successfully")
        void shouldRemoveStockSuccessfully() {
            // Given
            Product product = Product.create(VALID_NAME, VALID_DESCRIPTION, VALID_PRICE, VALID_STOCK);

            // When
            product.removeStock(3);

            // Then
            assertThat(product.getStockQuantity()).isEqualTo(7);
        }

        @Test
        @DisplayName("should throw exception when removing more stock than available")
        void shouldThrowExceptionWhenRemovingMoreStockThanAvailable() {
            // Given
            Product product = Product.create(VALID_NAME, VALID_DESCRIPTION, VALID_PRICE, VALID_STOCK);

            // When/Then
            assertThatThrownBy(() -> product.removeStock(15))
                .isInstanceOf(InvalidDomainStateException.class)
                .hasMessageContaining("Insufficient stock")
                .hasMessageContaining("Available: 10")
                .hasMessageContaining("Requested: 15");
        }

        @Test
        @DisplayName("should throw exception when removing zero or negative stock")
        void shouldThrowExceptionWhenRemovingZeroOrNegativeStock() {
            // Given
            Product product = Product.create(VALID_NAME, VALID_DESCRIPTION, VALID_PRICE, VALID_STOCK);

            // When/Then
            assertThatThrownBy(() -> product.removeStock(0))
                .isInstanceOf(InvalidDomainStateException.class)
                .hasMessageContaining("Quantity to remove must be positive");

            assertThatThrownBy(() -> product.removeStock(-5))
                .isInstanceOf(InvalidDomainStateException.class)
                .hasMessageContaining("Quantity to remove must be positive");
        }
    }

    @Nested
    @DisplayName("Product Information")
    class ProductInformation {

        @Test
        @DisplayName("should update product information")
        void shouldUpdateProductInformation() {
            // Given
            Product product = Product.create(VALID_NAME, VALID_DESCRIPTION, VALID_PRICE, VALID_STOCK);
            String newName = "Updated Product";
            String newDescription = "Updated Description";

            // When
            product.updateInfo(newName, newDescription);

            // Then
            assertThat(product.getName()).isEqualTo(newName);
            assertThat(product.getDescription()).isEqualTo(newDescription);
        }

        @Test
        @DisplayName("should throw exception when updating with invalid name")
        void shouldThrowExceptionWhenUpdatingWithInvalidName() {
            // Given
            Product product = Product.create(VALID_NAME, VALID_DESCRIPTION, VALID_PRICE, VALID_STOCK);

            // When/Then
            assertThatThrownBy(() -> product.updateInfo(null, "New Description"))
                .isInstanceOf(InvalidDomainStateException.class)
                .hasMessageContaining("Product name cannot be null or blank");

            assertThatThrownBy(() -> product.updateInfo("   ", "New Description"))
                .isInstanceOf(InvalidDomainStateException.class)
                .hasMessageContaining("Product name cannot be null or blank");
        }
    }

    @Nested
    @DisplayName("Product Status")
    class ProductStatusManagement {

        @Test
        @DisplayName("should activate product")
        void shouldActivateProduct() {
            // Given
            Product product = Product.create(VALID_NAME, VALID_DESCRIPTION, VALID_PRICE, VALID_STOCK);
            product.deactivate();

            // When
            product.activate();

            // Then
            assertThat(product.getStatus()).isEqualTo(ProductStatus.ACTIVE);
        }

        @Test
        @DisplayName("should deactivate product")
        void shouldDeactivateProduct() {
            // Given
            Product product = Product.create(VALID_NAME, VALID_DESCRIPTION, VALID_PRICE, VALID_STOCK);

            // When
            product.deactivate();

            // Then
            assertThat(product.getStatus()).isEqualTo(ProductStatus.INACTIVE);
        }

        @Test
        @DisplayName("should be available when active and has stock")
        void shouldBeAvailableWhenActiveAndHasStock() {
            // Given
            Product product = Product.create(VALID_NAME, VALID_DESCRIPTION, VALID_PRICE, VALID_STOCK);

            // When/Then
            assertThat(product.isAvailable()).isTrue();
        }

        @Test
        @DisplayName("should not be available when inactive")
        void shouldNotBeAvailableWhenInactive() {
            // Given
            Product product = Product.create(VALID_NAME, VALID_DESCRIPTION, VALID_PRICE, VALID_STOCK);
            product.deactivate();

            // When/Then
            assertThat(product.isAvailable()).isFalse();
        }

        @Test
        @DisplayName("should not be available when stock is zero")
        void shouldNotBeAvailableWhenStockIsZero() {
            // Given
            Product product = Product.create(VALID_NAME, VALID_DESCRIPTION, VALID_PRICE, 0);

            // When/Then
            assertThat(product.isAvailable()).isFalse();
        }
    }

    @Nested
    @DisplayName("Domain Events")
    class DomainEvents {

        @Test
        @DisplayName("should clear domain events")
        void shouldClearDomainEvents() {
            // Given
            Product product = Product.create(VALID_NAME, VALID_DESCRIPTION, VALID_PRICE, VALID_STOCK);
            assertThat(product.getDomainEvents()).isNotEmpty();

            // When
            product.clearDomainEvents();

            // Then
            assertThat(product.getDomainEvents()).isEmpty();
        }

        @Test
        @DisplayName("should return immutable copy of domain events")
        void shouldReturnImmutableCopyOfDomainEvents() {
            // Given
            Product product = Product.create(VALID_NAME, VALID_DESCRIPTION, VALID_PRICE, VALID_STOCK);
            List<DomainEvent> events = product.getDomainEvents();

            // When/Then
            assertThatThrownBy(() -> events.clear())
                .isInstanceOf(UnsupportedOperationException.class);
        }

        @Test
        @DisplayName("should accumulate multiple domain events")
        void shouldAccumulateMultipleDomainEvents() {
            // Given
            Product product = Product.create(VALID_NAME, VALID_DESCRIPTION, VALID_PRICE, VALID_STOCK);
            product.clearDomainEvents();

            // When
            Money newPrice = Money.of(BigDecimal.valueOf(150.00), Currency.getInstance("USD"));
            product.changePrice(newPrice);
            product.addStock(5);

            // Then
            List<DomainEvent> events = product.getDomainEvents();
            assertThat(events).hasSize(1); // Only price change registers an event
            assertThat(events.get(0)).isInstanceOf(ProductPriceChangedEvent.class);
        }
    }

    @Nested
    @DisplayName("Reconstitution")
    class Reconstitution {

        @Test
        @DisplayName("should reconstitute product from persistence")
        void shouldReconstituteProductFromPersistence() {
            // Given
            ProductId id = ProductId.generate();
            Instant createdAt = Instant.now().minusSeconds(3600);
            Instant updatedAt = Instant.now();

            // When
            Product product = new Product(
                id,
                VALID_NAME,
                VALID_DESCRIPTION,
                VALID_PRICE,
                VALID_STOCK,
                ProductStatus.ACTIVE,
                createdAt,
                updatedAt
            );

            // Then
            assertThat(product.getId()).isEqualTo(id);
            assertThat(product.getName()).isEqualTo(VALID_NAME);
            assertThat(product.getDescription()).isEqualTo(VALID_DESCRIPTION);
            assertThat(product.getPrice()).isEqualTo(VALID_PRICE);
            assertThat(product.getStockQuantity()).isEqualTo(VALID_STOCK);
            assertThat(product.getStatus()).isEqualTo(ProductStatus.ACTIVE);
            assertThat(product.getCreatedAt()).isEqualTo(createdAt);
            assertThat(product.getUpdatedAt()).isEqualTo(updatedAt);
        }

        @Test
        @DisplayName("should not register domain events when reconstituting")
        void shouldNotRegisterDomainEventsWhenReconstituting() {
            // Given
            ProductId id = ProductId.generate();
            Instant createdAt = Instant.now().minusSeconds(3600);
            Instant updatedAt = Instant.now();

            // When
            Product product = new Product(
                id,
                VALID_NAME,
                VALID_DESCRIPTION,
                VALID_PRICE,
                VALID_STOCK,
                ProductStatus.ACTIVE,
                createdAt,
                updatedAt
            );

            // Then
            assertThat(product.getDomainEvents()).isEmpty();
        }
    }

    @Nested
    @DisplayName("jMolecules Integration")
    class JMoleculesIntegration {

        @Test
        @DisplayName("should implement AggregateRoot interface")
        void shouldImplementAggregateRootInterface() {
            // Given
            Product product = Product.create(VALID_NAME, VALID_DESCRIPTION, VALID_PRICE, VALID_STOCK);

            // When/Then
            assertThat(product).isInstanceOf(org.jmolecules.ddd.types.AggregateRoot.class);
        }

        @Test
        @DisplayName("should have ProductId as Identifier")
        void shouldHaveProductIdAsIdentifier() {
            // Given
            Product product = Product.create(VALID_NAME, VALID_DESCRIPTION, VALID_PRICE, VALID_STOCK);

            // When/Then
            assertThat(product.getId()).isInstanceOf(org.jmolecules.ddd.types.Identifier.class);
        }

        @Test
        @DisplayName("should have @Identity annotation on id field")
        void shouldHaveIdentityAnnotationOnIdField() throws NoSuchFieldException {
            // When
            var idField = Product.class.getDeclaredField("id");

            // Then
            assertThat(idField.isAnnotationPresent(org.jmolecules.ddd.annotation.Identity.class)).isTrue();
        }
    }
}
