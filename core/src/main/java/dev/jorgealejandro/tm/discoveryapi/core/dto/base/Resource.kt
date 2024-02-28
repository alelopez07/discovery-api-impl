package dev.jorgealejandro.tm.discoveryapi.core.dto.base

import dev.jorgealejandro.tm.discoveryapi.core.dto.constants.ConnectionStateConstants
import dev.jorgealejandro.tm.discoveryapi.core.dto.constants.StatusConstants as Status

/**
 * [Resource]
 * Class representing a resource that can be in one of three states:
 *
 * * [Status.SUCCESS]: The resource is successfully loaded and available.
 * * [Status.ERROR]: The resource could not be loaded due to an error.
 * * [Status.LOADING]: The resource is currently being loaded.
 * * [Status.NO_CONNECTION]: There is no internet connection and the resource cannot be loaded.
 *
 * This class is used to represent the state of data that is loaded from a remote source, such as
 * a network API. It allows you to easily handle different states of the data and display
 * appropriate UI elements to the user.
 *
 * @param status The current status of the resource.
 * @param data The data associated with the resource, if available.
 * @param message A message describing the status of the resource, if available.
 */
data class Resource<out T>(
    val status: Status,
    val data: T?,
    val message: String?
) {

    companion object {

        /**
         * Creates a successful resource with the given data.
         *
         * @param data The data associated with the resource.
         * @param status The status of the resource. Defaults to [Status.SUCCESS].
         * @return A new [Resource] object with the given data and status.
         */
        fun <T> success(
            data: T?,
            status: ConnectionStateConstants = ConnectionStateConstants.ONLINE
        ): Resource<T> {
            return Resource(
                status = Status.SUCCESS,
                data = data,
                message = status.message
            )
        }

        /**
         * Creates an error resource with the given error message.
         *
         * @param errorMessage The error message associated with the resource.
         * @param data The data associated with the resource, if available. Defaults to <null>.
         * @return A new [Resource] object with the given error message and data.
         */
        fun <T> error(
            errorMessage: String?,
            data: T? = null
        ): Resource<T> {
            return Resource(
                status = Status.ERROR,
                data = data,
                message = errorMessage
            )
        }

        /**
         * Creates a loading resource with the given data.
         *
         * @param data The data associated with the resource, if available. Defaults to <null>.
         * @return A new [Resource] object with the given data and status.
         */
        fun <T> loading(data: T? = null): Resource<T> {
            return Resource(
                status = Status.LOADING,
                data = data,
                message = null
            )
        }

        /**
         * Creates a no internet resource with the given data.
         *
         * @param data The data associated with the resource, if available. Defaults to <null>.
         * @return A new [Resource] object with the given data and status.
         */
        fun <T> noInternet(data: T? = null): Resource<T> {
            return Resource(
                status = Status.NO_CONNECTION,
                data = data,
                message = ConnectionStateConstants.OFFLINE.message
            )
        }
    }
}