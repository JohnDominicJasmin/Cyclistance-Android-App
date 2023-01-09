package cyclistance.repositories

import com.example.cyclistance.feature_mapping.data.remote.dto.rescue_transaction.Route
import com.example.cyclistance.feature_mapping.data.remote.dto.rescue_transaction.Status
import com.example.cyclistance.feature_mapping.data.remote.dto.user_dto.*
import com.example.cyclistance.feature_mapping.domain.exceptions.MappingExceptions
import com.example.cyclistance.feature_mapping.domain.model.*
import com.example.cyclistance.feature_mapping.domain.repository.MappingRepository
import com.mapbox.geojson.Point
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeMappingRepository: MappingRepository {


    private val users = mutableListOf(
        UserItem(
            address = "Manila, Quiapo",
            contactNumber = "09123456789",
            id = "1",
            location = Location(latitude = 14.599512, longitude = 120.984222),
            name = "Andres",
            profilePictureUrl = "https://i.imgur.com/1ZQ3Y7r.jpg",
            rescueRequest = RescueRequest(
                respondents = listOf(
                    Respondent(clientId = "2"),
                    Respondent(clientId = "3"))
            ),
            transaction = Transaction(role = "rescuee", transactionId = "12345"),
            userAssistance = UserAssistance(
                confirmationDetail = ConfirmationDetail(
                    bikeType = "road-bike",
                    description = "Sample description",
                    message = "I need help",
                ),
                needHelp = true,
            ),

            ),
        UserItem(
            address = "Manila, Quiapo",
            contactNumber = "09123456789",
            id = "2",
            location = Location(latitude = 14.599512, longitude = 120.984222),
            name = "Andres",
            profilePictureUrl = "https://i.imgur.com/1ZQ3Y7r.jpg",
            rescueRequest = RescueRequest(
            ),
            transaction = Transaction(role = "rescuee", transactionId = "12345"),
            userAssistance = UserAssistance(
                confirmationDetail = ConfirmationDetail(
                    bikeType = "road-bike",
                    description = "Sample description",
                    message = "I need help",
                ),
                needHelp = true,
            ),

            ),
        UserItem(
            address = "Manila, Quiapo",
            contactNumber = "09123456789",
            id = "3",
            location = Location(latitude = 14.599512, longitude = 120.984222),
            name = "Andres",
            profilePictureUrl = "https://i.imgur.com/1ZQ3Y7r.jpg",
            rescueRequest = RescueRequest(),
            transaction = Transaction(role = "rescuee", transactionId = "12345"),
            userAssistance = UserAssistance(
                confirmationDetail = ConfirmationDetail(
                    bikeType = "road-bike",
                    description = "Sample description",
                    message = "I need help",
                ),
                needHelp = true,
            ))

        )


    private val rescueTransactions = listOf(
        RescueTransactionItem(
            id = "12345",
            rescueeId = "1",
            rescuerId = "2",
            status = Status(ongoing = true, started = true),
            route = Route(
                startingLocation = Location(latitude = 14.599512, longitude = 120.984222),
                destinationLocation = Location(latitude = 14.599512, longitude = 120.984222),
            ),
        )
    )

    private val bikeType = MutableStateFlow("")
    private val address = MutableStateFlow("")
    private val location = MutableStateFlow(Location())
    private val user = MutableStateFlow(User())
    private val rescueTransaction = MutableStateFlow(RescueTransaction())
    private val liveLocation = MutableStateFlow(LiveLocationWSModel())

    private var shouldReturnNetworkError = false

    fun shouldReturnNetworkError(value: Boolean){
        shouldReturnNetworkError = value
    }

    override suspend fun getUserById(userId: String): UserItem {
        if(shouldReturnNetworkError){
            throw MappingExceptions.NetworkException()
        }
        return users.find { it.id == userId } ?: throw MappingExceptions.UserException()
    }

    override suspend fun getUsers(): User {
        if(shouldReturnNetworkError){
            throw MappingExceptions.NetworkException()
        }
        return User(users)
    }

    override suspend fun createUser(userItem: UserItem) {

        if(shouldReturnNetworkError){
            throw MappingExceptions.NetworkException()
        }

        if (users.find { it.id == userItem.id } != null) {
            throw MappingExceptions.UserException("User already exists")
        }

        users.add(userItem)
    }

    override suspend fun deleteUser(id: String) {

        if(shouldReturnNetworkError){
            throw MappingExceptions.NetworkException()
        }

        if(users.find { it.id == id } == null) {
            throw MappingExceptions.UserException()
        }

        users.removeIf { it.id == id }

    }




    override suspend fun deleteRescueRespondent(userId: String, respondentId: String) {

        if(shouldReturnNetworkError){
            throw MappingExceptions.NetworkException()
        }

        val userFound = users.find { it.id == userId } ?: throw MappingExceptions.UserException()
        userFound.rescueRequest?.respondents?.toMutableList()
            ?.removeIf { it.clientId == respondentId }
    }

    override suspend fun addRescueRespondent(userId: String, respondentId: String) {

        if(shouldReturnNetworkError){
            throw MappingExceptions.NetworkException()
        }

        val userFound = users.find { it.id == userId } ?: throw MappingExceptions.UserException()
        userFound.rescueRequest?.respondents?.toMutableList()
            ?.add(Respondent(clientId = respondentId))
    }

    override suspend fun deleteAllRespondents(userId: String) {

        if(shouldReturnNetworkError){
            throw MappingExceptions.NetworkException()
        }

        val userFound = users.find { it.id == userId } ?: throw MappingExceptions.UserException()
        userFound.rescueRequest?.respondents?.toMutableList()?.clear()
    }




    override suspend fun getRescueTransactionById(transactionId: String): RescueTransactionItem {

        if(shouldReturnNetworkError){
            throw MappingExceptions.NetworkException()
        }

        return rescueTransactions.find { it.id == transactionId }
                ?: throw MappingExceptions.RescueTransactionException("Rescue transaction not found")

    }

    override suspend fun createRescueTransaction(rescueTransaction: RescueTransactionItem) {

        if(shouldReturnNetworkError){
            throw MappingExceptions.NetworkException()
        }

        if (rescueTransactions.find { it.id == rescueTransaction.id } != null) {
            throw MappingExceptions.RescueTransactionException("Rescue transaction already exists")
        }

        rescueTransactions.toMutableList().add(rescueTransaction)
    }

    override suspend fun deleteRescueTransaction(transactionId: String) {

        if(shouldReturnNetworkError){
            throw MappingExceptions.NetworkException()
        }

        if(rescueTransactions.find { it.id == transactionId } == null) {
            throw MappingExceptions.RescueTransactionException("Rescue transaction not found")
        }

        rescueTransactions.toMutableList().removeIf { it.id == transactionId }
    }




    override suspend fun getBikeType(): Flow<String> {
        return bikeType.asStateFlow()
    }

    override suspend fun setBikeType(bikeType: String) {
        this.bikeType.value = bikeType
    }

    override suspend fun getAddress(): Flow<String> {
        return address.asStateFlow()
    }

    override suspend fun setAddress(address: String) {
        this.address.value = address
    }

    override suspend fun getUserLocation(): Flow<Location> {
        return location.asStateFlow()
    }

    override suspend fun getUserUpdates(): Flow<User> {
        return user.asStateFlow()
    }

    override suspend fun getRescueTransactionUpdates(): Flow<RescueTransaction> {
        return rescueTransaction.asStateFlow()
    }

    override suspend fun getTransactionLocationUpdates(): Flow<LiveLocationWSModel> {
        return liveLocation.asStateFlow()
    }

    override suspend fun broadcastUser() {

        if(shouldReturnNetworkError){
            throw MappingExceptions.NetworkException()
        }

        print("broadcastLocation")
    }

    override suspend fun broadcastRescueTransaction() {

        if(shouldReturnNetworkError){
            throw MappingExceptions.NetworkException()
        }

        print("broadcastRescueTransaction")
    }

    override suspend fun broadcastLocation(locationModel: LiveLocationWSModel) {

        if(shouldReturnNetworkError){
            throw MappingExceptions.NetworkException()
        }

        print("broadcastLocation")
    }

    override suspend fun getRouteDirections(origin: Point, destination: Point): RouteDirection {

        if(shouldReturnNetworkError){
            throw MappingExceptions.NetworkException()
        }

        return RouteDirection(geometry = "test-geometry", duration = 1000.0)
    }
}